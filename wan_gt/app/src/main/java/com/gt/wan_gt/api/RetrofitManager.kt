package com.gt.wan_gt.api

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.api.CommonInterceptor.CodeCallBack
import com.gt.wan_gt.common.PathManager
import com.gt.wan_gt.utils.NetWorkUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.IllegalArgumentException
import java.net.FileNameMap
import java.util.concurrent.TimeUnit

/**
 * time 2020/7/15 0015
 * author GT
 */
object RetrofitManager {

    private const val CACHE_MAX_LENGTH = 10 * 1024 * 1024L //缓存长度10M

    private var okHttpClient:OkHttpClient? = null
    private const val DEFAULT_TIMEOUT = 10L
    private val commonInterceptor = CommonInterceptor()

    /**
     * 初始化
     */
    fun initRetrofit() {
        if(okHttpClient != null) return
        val cache = Cache(File(PathManager.CACHE_DIR), CACHE_MAX_LENGTH)
        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            if(!NetWorkUtil.isNetConnect()){//没有网络强制使用缓存
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE).build()
            }
            var response = chain.proceed(request)
            response = if(NetWorkUtil.isNetConnect()){
                //有网 不缓存 保存最大时长
                val maxAge = 60
                response.newBuilder()
                    .header("Cache-Control","public, max-age=$maxAge")
                    .removeHeader("Pragma")
                    .build()
            }else{
                //无网络时，设置超时为1周
                val maxStale = 60 * 60 *24 *7
                response.newBuilder()
                    .header("Cache-Control","public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            }
            response
        }
        okHttpClient = OkHttpClient.Builder()
//            .eventListenerFactory(HttpEventListener.FACTORY)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(getHttpLoggingInterceptor())
            .addNetworkInterceptor(cacheInterceptor)
            .cache(cache)
//                .addInterceptor(commonInterceptor)
            .cookieJar(PersistentCookieJar(SetCookieCache(),
                SharedPrefsCookiePersistor(WanApp.instance)))
            .build()


    }

    /**
     * 创建接口对象
     */
    fun <T> create(baseUrl: String="", service: Class<T>): T {
        val mRetrofitBuilder = Retrofit.Builder() //添加CallAdapterFactory 用Observable<ResponseBody>接收 添加自定义CallAdapterFactory 则用用Observable<T>接收
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加ConverterFactory 用Call<ResponseBody>接收 添加自定义ConverterFactory 则用Call<T>接收
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient!!)
        mRetrofitBuilder.baseUrl(baseUrl)
        return mRetrofitBuilder.build().create(service)
    }


    /**
     * 设置一些公共code的回调
     *
     */
    fun setCommonCallback(commonCallback: CodeCallBack?) {
        commonInterceptor.setCodeCallBack(commonCallback)
    }

    /**
     * 日志拦截器
     */
    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.e("OkHttp", "log message = $message"
                )
            })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
}