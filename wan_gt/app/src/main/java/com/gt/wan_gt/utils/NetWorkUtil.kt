package com.gt.wan_gt.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telecom.ConnectionService
import com.gt.wan_gt.WanApp

/**
 * time 2020/7/15 0015
 * author GT
 */
object NetWorkUtil {
    /**
     * 网络是否连接
     */
    fun isNetConnect():Boolean{
        val manager  = WanApp.instance?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo != null
    }
}