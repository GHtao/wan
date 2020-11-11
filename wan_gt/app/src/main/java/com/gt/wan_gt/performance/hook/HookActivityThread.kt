package com.gt.wan_gt.performance.hook

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log

/**
 * time 2020/8/11 0011
 * author GT
 * hook activity thread 中的handler
 */
object HookActivityThread {

    @SuppressLint("PrivateApi")
    fun hookHandler(){
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityField = activityThreadClass.getDeclaredField("sCurrentActivityThread")
        activityField.isAccessible = true

        val mHField = activityThreadClass.getDeclaredField("mH")
        mHField.isAccessible = true
        val handler = mHField.get(activityField.get(null)) as Handler

        val handlerClass = Class.forName("android.os.Handler")
        val callBackField = handlerClass.getDeclaredField("mCallback")
        callBackField.isAccessible = true

        val queueWorkClass = Class.forName("android.app.QueuedWork")
        val finisherField = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            queueWorkClass.getDeclaredField("sPendingWorkFinishers")
        }else{
            queueWorkClass.getDeclaredField("sFinishers")
        }
        finisherField.isAccessible = true

        val linkedClass = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1){
            Class.forName("java.util.concurrent.ConcurrentLinkedQueue")
        }else{
            Class.forName("java.util.LinkedList")
        }
        val clearMethod = linkedClass.getMethod("clear")
        callBackField.set(handler,object :Handler.Callback{
            override fun handleMessage(msg: Message?): Boolean {
                //获取QueueWork 来清空 防止在activity stop ,service stop的时候anr
//                当SP有未同步到磁盘的工作，则需等待其完成，才告知系统已完成该广播。
//                并且只有XML静态注册的广播超时检测过程会考虑是否有SP尚未完成，动态广播并不受其影响
//                Log.e("gt","清空finisher")
                clearMethod.invoke(finisherField.get(null))
                return false
            }
        })
    }
}