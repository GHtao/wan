package com.gt.wan_gt.performance.hook

import android.util.Log
import android.view.View
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import java.lang.Exception

/**
 * time 2020/8/14 0014
 * author GT
 * 防止view两次点击
 */
const val TIME_GAP = 1000L
@Aspect
class HookQuickClick {
    private var lastClickTime = 0L
    private var lastView: View? = null//上次点击的view

    @Around("execution (* android.view.View.OnClickListener.onClick(..))")
    fun quickClick(joinPoint: ProceedingJoinPoint){
        if(System.currentTimeMillis() - lastClickTime >= TIME_GAP){
            //可以点击
//            Log.e("gt","正常执行点击事件")
            doClick(joinPoint)
        }else{
            if(lastView == null || lastView!! != joinPoint.args[0]){
                //可以点击
                doClick(joinPoint)
//                Log.e("gt","不是同一个view的快速点击事件")
            }
//            else{
                //不可以点击
//                Log.e("gt","拦截两次快速点击事件")
//            }
        }
    }

    @Around("execution (* com.gt.core.base.BaseActivity.onCreate(..))")
    fun activityCreate(joinPoint: ProceedingJoinPoint){
        logTime(joinPoint)
    }

    /**
     * 注意有返回值的要 函数要带返回值
     */
    @Around("execution (* com.gt.core.base.BaseFragment.onCreateView(..))")
    fun viewCreate(joinPoint: ProceedingJoinPoint):Any{
        return logTime(joinPoint)
    }

    /**
     * 记录方法执行时间
     */
    private fun logTime(joinPoint: ProceedingJoinPoint):Any{
        val name = joinPoint.target.javaClass.name
        val startTime = System.currentTimeMillis()
        var proceed = Any()
        try {
            proceed = joinPoint.proceed()
        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.e("gt","$name createView:${(System.currentTimeMillis() - startTime)}")
        return proceed
    }

    /**
     * 处理点击事件
     */
    private fun doClick(joinPoint: ProceedingJoinPoint){
        if(joinPoint.args.isEmpty()){
            joinPoint.proceed()
            return
        }
        lastView = joinPoint.args[0] as View
        lastClickTime = System.currentTimeMillis()
        try {
            joinPoint.proceed()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}