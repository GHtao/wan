package com.gt.wan_gt.performance.printer

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import java.lang.StringBuilder

/**
 * time 2020/8/13 0013
 * author GT
 * 延时任务来打印堆栈信息
 */
object PrinterLog {
    private const val TIME = 1000L//卡顿阈值
    private val handlerThread = HandlerThread("printer-log")
    private var mHandler:Handler
    private val stringBuilder = StringBuilder()

    init {
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
    }
    private val runnable = Runnable {
        stringBuilder.clear()
        Looper.getMainLooper().thread.stackTrace.forEach {
            stringBuilder.append(it)
            stringBuilder.append("\n")
            Log.e("gt","卡顿日志:${stringBuilder}")
        }
    }
    /**
     * 发送延时任务
     */
    fun sendDelayMessage(){
        if(handlerThread.isAlive){
            mHandler.postDelayed(runnable, TIME)
        }
    }

    /**
     * 取消延时任务
     */
    fun cancelDelayMessage(){
        mHandler.removeCallbacks(runnable)
    }

    /**
     * 退出循环
     */
    fun quit(){
        handlerThread.quit()
    }
}