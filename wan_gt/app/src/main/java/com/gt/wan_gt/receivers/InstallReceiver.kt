package com.gt.wan_gt.receivers

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.gt.wan_gt.splash.SplashActivity

/**
 * time 2020/10/28 0028
 * author GT
 */
class InstallReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action.equals(Intent.ACTION_PACKAGE_REPLACED)){
            Log.e("gt","应用覆盖安装")
        }

    }
}