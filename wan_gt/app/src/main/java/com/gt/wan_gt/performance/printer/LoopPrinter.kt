package com.gt.wan_gt.performance.printer

import android.os.Looper
import android.util.Log
import android.util.Printer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder

/**
 * time 2020/8/13 0013
 * author GT
 * 使用printer来检测卡顿
 */
class LoopPrinter :Printer {
    companion object{
        const val START = ">>>>> Dispatching to"
        const val END = "<<<<< Finished to"
    }
    override fun println(x: String?) {
        x?.also {
            when{
                it.contains(START)->{
                    //开始一个1s的延时任务
                    PrinterLog.sendDelayMessage()
                }
                it.contains(END)->{
                    //不足1s直接取消掉
                    PrinterLog.cancelDelayMessage()
                }
            }
        }
    }


}