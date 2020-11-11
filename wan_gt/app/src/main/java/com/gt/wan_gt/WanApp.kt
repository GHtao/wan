package com.gt.wan_gt

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.*
import com.alibaba.android.arouter.launcher.ARouter
import com.github.anrwatchdog.ANRWatchDog
import com.gt.wan_gt.api.OrcApi
import com.gt.wan_gt.api.RetrofitManager
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.common.PathManager
import com.gt.wan_gt.common.WanMMKV
import com.gt.wan_gt.db.DbManager
import com.gt.wan_gt.performance.hook.HookActivityThread
import com.gt.wan_gt.performance.printer.LoopPrinter
import com.gt.wan_gt.performance.tencent.DynamicConfigImplDemo
import com.gt.wan_gt.performance.tencent.MatrixPluginListener
import com.gt.wan_gt.receivers.InstallReceiver
import com.gt.wan_gt.splash.SplashActivity
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.workers.UploadLogWorker
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.resource.ResourcePlugin
import com.tencent.matrix.resource.config.ResourceConfig
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xcrash.XCrash
import java.util.concurrent.TimeUnit

/**
 * time 2020/7/13 0013
 * author GT
 */
class WanApp:Application() {
    companion object{
        var instance:WanApp? = null
        var isFirstRun = true
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        XCrash.init(this, XCrash.InitParameters().setLogDir(PathManager.CRASH_LOG_DIR))
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        ToastUtil.init(this)
        RetrofitManager.initRetrofit()
        WanApi.initApi()
        OrcApi.initApi()
        DbManager.initDb(this)
        val mmkvDir = MMKV.initialize(this)

        Log.e("gt","mmkv dir:$mmkvDir")
//        initWorkManager()
        GlobalScope.launch {
            HookActivityThread.hookHandler()
        }
        initANRWatch()
        mainLooper.setMessageLogging(LoopPrinter())


        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(object :LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onAppBackground(){
                Log.e("gt","应用进入后台")
                //禁用之前的launch
                //因为已经禁用过了Splash activity 再次覆盖安装的时候会有问题 提示招不到SplashActivity
//                packageManager.setComponentEnabledSetting(
//                    ComponentName(packageName,SplashActivity::class.java.name),
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP)
//                //启用我们设置的别名
//                packageManager.setComponentEnabledSetting(
//                    ComponentName(packageName,"com.gt.icon"),
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onAppForeground(){
                Log.e("gt","应用进入前台")
            }
        })

//        initMatrix()
        initARoute()
    }

    private fun initARoute(){
        ARouter.init(this)
    }
    /**
     * 初始化 matrix的监控
     */
    private fun initMatrix(){
        //卡顿耗时 慢函数 启动耗时
        val dynamicConfigImplDemo = DynamicConfigImplDemo()
        val tracePlugin = TracePlugin(
            TraceConfig.Builder()
                .dynamicConfig(dynamicConfigImplDemo)
                .enableAnrTrace(true)
                .enableFPS(true)
                .enableEvilMethodTrace(true)
                .enableStartup(true)
                .build()
        )
        //性能  泄露
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfigImplDemo)
                .build()
        )
        //资源检测
        val resourcePlugin = ResourcePlugin(
            ResourceConfig.Builder()
                .dynamicConfig(dynamicConfigImplDemo)
                .setAutoDumpHprofMode(ResourceConfig.DumpMode.AUTO_DUMP)
                .setDetectDebuger(false)
                .build()
        )

        val builder = Matrix.Builder(this)
            .patchListener(MatrixPluginListener(this))
            .plugin(ioCanaryPlugin)
            .plugin(tracePlugin)
            .plugin(resourcePlugin)
            .build()

        Matrix.init(builder)
        tracePlugin.start()
        ioCanaryPlugin.start()
        resourcePlugin.start()

    }
    /**
     * 初始化anr 监控
     */
    private fun initANRWatch(){
        ANRWatchDog(10000).setANRListener {
            //可以保存anr信息 再上传  默认是抛出异常
            Log.e("gt","anr log:${it.printStackTrace()}")
        }.start()
    }
    /**
     * 初始化work manager中的任务
     */
    private fun initWorkManager(){
        //可以给任务添加约束条件
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)//有网的时候再执行
            .build()

        val workData = workDataOf(Pair("file_path","path"))

        val workRequest = OneTimeWorkRequestBuilder<UploadLogWorker>()
            .setConstraints(constraints)
            .setInputData(workData)//设置work的参数
            .setInitialDelay(60,TimeUnit.SECONDS)//延时60s之后执行
            //doWork返回retry 会延时执行
             .setBackoffCriteria(BackoffPolicy.LINEAR,10,TimeUnit.SECONDS)
            .addTag("file_upload")//添加标记 可以用于取消 或者是获取
            .build()

        //周期性任务 最小是15分钟执行一次
        val periodWorker = PeriodicWorkRequestBuilder<UploadLogWorker>(10,TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(workData)//设置work的参数
            .setBackoffCriteria(BackoffPolicy.LINEAR,10,TimeUnit.SECONDS)
            .addTag("file_upload")//添加标记 可以用于取消 或者是获取
            .build()
        WorkManager.getInstance(this).cancelAllWork()
        WorkManager.getInstance(this).cancelAllWorkByTag("file_upload")
//        WorkManager.getInstance(this).enqueue(periodWorker)

        //定义从属关系 先执行workRequest再执行workRequest2
        //第一个失败或者取消后面的就都失败或者取消
//        WorkManager.getInstance(this)
//            .beginWith(listOf(filter1, filter2, filter3)) 这里面的三个可能会并行执行
//            .beginWith(workRequest)
//            .then(workRequest2)
//            .enqueue()


        //可以获取到worker观察worker的状态
//        WorkManager.getInstance(this)
//            .getWorkInfosByTagLiveData("file_upload")
//            .observe(this, Observer {
//                Log.e("gt","work info:${it[0].state}")
//            })
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}



