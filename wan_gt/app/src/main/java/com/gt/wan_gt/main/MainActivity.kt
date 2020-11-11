package com.gt.wan_gt.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.gt.core.base.BaseActivity
import com.gt.wan_gt.R
import com.gt.wan_gt.common.event.EventBean
import com.gt.wan_gt.index.IndexFragment
import com.gt.wan_gt.orc.OrcFragment
import com.gt.wan_gt.orc.REQUEST_CODE_CONTENT
import com.gt.wan_gt.performance.printer.PrinterLog
import com.gt.wan_gt.test.TestFragment
import com.gt.wan_gt.utils.FileChooseUtil
import com.gt.wan_gt.utils.StatusBarUtil
import com.gt.wan_gt.views.AppFloatView
import com.gt.wan_gt.web_view.WebViewActivity
import me.yokeyword.fragmentation.ISupportFragment
import org.greenrobot.eventbus.EventBus
import java.util.*

class MainActivity : BaseActivity<MainActivityViewModel>() {
    private val KEY_REQUEST_CODE = 100
    private val permissions = arrayOf(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.SYSTEM_ALERT_WINDOW,
        Manifest.permission.READ_SMS,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.BLUETOOTH_ADMIN
    )
    override fun contentView(savedInstanceState: Bundle?) = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        initPermissions()
        StatusBarUtil.setStatusColor(window,
            ContextCompat.getColor(this, R.color.main_status_bar_blue),
            1f)

//        ARouter.getInstance()
//            .build("/test/route")
//            .withString("aaa","bundle aaa")
//            .navigation()
//        loadRootFragment(R.id.fl_content, IndexFragment())
        loadRootFragment(R.id.fl_content, TestFragment())
//        AppFloatView.show()

    }

    /**
     * 初始化权限
     */
    private fun initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val list = ArrayList<String>()
            for (permission in permissions) {
                if (applicationContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission)
                }
            }
            if (list.size > 0) {
                requestPermissions(
                    list.toTypedArray(),
                    KEY_REQUEST_CODE
                )
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            when(requestCode){
                WebViewActivity.WEB_REQUEST_CODE->{
                    EventBus.getDefault().post(EventBean(EventBean.Type.COLLECT,
                            data.getBooleanExtra(WebViewActivity.ACTIVITY_COLLECT_RETURN,false)))

                }
                REQUEST_CODE_CONTENT->{
                    val uri = data.data
                    val path = FileChooseUtil.getInstance(applicationContext)
                        .getChooseFileResultPath(uri)
                    EventBus.getDefault()
                        .postSticky(EventBean(EventBean.Type.ORC_PATH,path))
                    Log.e("gt","发送path:$path")
                }
            }
        }
    }
    override fun createVM(): MainActivityViewModel {
        return ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }


    override fun observe() {
        viewModel.apply {

        }
    }

    /**
     * 启动跟index同级的fragment
     */
    fun startFragment(to:ISupportFragment){
        start(to)
    }

    override fun onDestroy() {
        super.onDestroy()
//        AppFloatView.hide()
        mainLooper.setMessageLogging(null)
        PrinterLog.quit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.e("gt","onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }
}