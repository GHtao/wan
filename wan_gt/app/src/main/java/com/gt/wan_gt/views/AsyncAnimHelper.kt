package com.gt.wan_gt.views

import android.view.View
import android.view.ViewPropertyAnimator

/**
 * time 2020/10/13 0013
 * author GT
 * 异步执行view动画
 * ViewPropertyAnimator 不能有UpdateListener Listener Actions否则还是执行属性动画
 * 使用RenderThread
 */
object AsyncAnimHelper {
    /**
     * 执行异步动画之前需要调用
     */
    fun onStartBefore(animator: ViewPropertyAnimator,view:View){
        createViewPropertyAnimRt(view)?.also {
            setViewPropertyAnimRt(animator,it)
        }
    }

    /**
     * 创建ViewPropertyAnimatorRT对象
     */
    private fun createViewPropertyAnimRt(view:View):Any?{
        try {
            val viewPropertyAnimClass =  Class.forName("android.view.ViewPropertyAnimatorRT")
            val constructor = viewPropertyAnimClass.getDeclaredConstructor(View::class.java)
            constructor.isAccessible = true
            return constructor.newInstance(view)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将ViewPropertyAnimatorRT对象设置给ViewPropertyAnimator
     */
    private fun setViewPropertyAnimRt(animator: ViewPropertyAnimator,animatorRt:Any){
        try {
            val animatorClass = Class.forName("android.view.ViewPropertyAnimator")
            val rtField = animatorClass.getDeclaredField("mRTBackend")
            rtField.isAccessible = true
            rtField.set(animator,animatorRt)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}