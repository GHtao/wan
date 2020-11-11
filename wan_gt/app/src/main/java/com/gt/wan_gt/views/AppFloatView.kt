package com.gt.wan_gt.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.utils.StatusBarUtil
import me.jessyan.autosize.utils.ScreenUtils

/**
 * time 2020/8/24 0024
 * author GT
 * app全局弹窗
 */
object AppFloatView {

    private var windowManager:WindowManager = WanApp.instance?.
            getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var floatView: View = LayoutInflater.from(WanApp.instance)
        .inflate(R.layout.layout_app_float,null)
    private var params: WindowManager.LayoutParams = WindowManager.LayoutParams()

    private var showFlag = false

    init {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            params.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        //位置
        params.x = 300
        params.y = 300
        Log.e("gt","params gravity:${params.gravity}")
        params.gravity = Gravity.START or Gravity.TOP
        // Window 不能获得输入焦点，即不接受任何按键或按钮事件，例如该 Window 上 有 EditView，点击 EditView 是 不会弹出软键盘的
        // Window 范围外的事件依旧为原窗口处理；例如点击该窗口外的view，依然会有响应。另外只要设置了此Flag，都将会启用FLAG_NOT_TOUCH_MODAL
        params.width = 100
        params.height = 100
        //背景变为透明
        params.format = PixelFormat.TRANSPARENT
        var x = 0f
        var y = 0f
        val topBarHeight = StatusBarUtil.getStatusBarHeight(WanApp.instance)
        floatView.setOnClickListener{v->
            Log.e("gt","float view click")
        }
        floatView.setOnTouchListener { v, event ->
            when(event.action){
                ACTION_DOWN->{
                    x = event.x
                    y = event.y
                }
                ACTION_MOVE->{
                    val newX = event.rawX
                    val newY = event.rawY
                    params.x = (newX - x).toInt()
                    params.y = (newY - y - topBarHeight).toInt()

                    windowManager.updateViewLayout(floatView, params)
                }
            }
            //有点击事件 需要继续往下传递
            false
        }

    }

    /**
     * 显示弹窗
     */
    fun show(){
        showFlag = true
        windowManager.addView(floatView,params)
    }

    /**
     * 隐藏弹窗
     */
    fun hide(){
        if(showFlag){
            showFlag = false
            windowManager.removeView(floatView)
        }
    }
}
