package com.gt.wan_gt.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.RequiresApi
import androidx.core.view.DisplayCompat
import kotlin.math.abs
import kotlin.math.min

/**
 * time 2020/10/22 0022
 * author GT
 * 刻度尺view
 */
class ScaleView @JvmOverloads
    constructor(context:Context,attributes: AttributeSet?=null,style:Int=0):
    View(context,attributes,style) {
    //最长的刻度
    private val largeLine = 50
    private val midLine = 40
    private val minLine = 25
    private val scaleWidth = 8//刻度的宽度
    //刻度值的最小值
    private val minSize = 0
    private val maxSize = 50
    private val paint = Paint()
    private val intervalSize = 6//刻度间隔距离
    private var centerY = 0
    private var centerX = 0
    private val textSize = 24f//文字高度
    private var offset = 100//起始位置偏移量
    private var moved = 0//移动的距离
    private var downX = 0f
    private var totalWidth = 0//控件的总宽度
    private var touchSlop:Int = ViewConfiguration.get(context).scaledTouchSlop
    private var currScale = (maxSize-minSize)/2
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerY = h/2
        //计算出控件的总宽度
        totalWidth = (maxSize - minSize)*(intervalSize+scaleWidth)+ scaleWidth
        centerX = w/2
        //初始居中显示
        offset = w/2 - totalWidth/2
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var left = offset + moved
        var right = 0
        val top = centerY
        var bottom = 0

        for(i in 0 .. maxSize){
            right = left+scaleWidth
            when {
                i % 10 == 0 -> {
                    bottom = centerY + largeLine
                    //绘制文字
                    val textWidth = paint.measureText(i.toString())
                    paint.textSize = textSize
                    canvas?.drawText(i.toString(),left.toFloat()-textWidth/2+scaleWidth/2,bottom+20f,paint)
                }
                i % 5 ==0 -> {
                    bottom = centerY + midLine
                }
                else -> {
                    bottom = centerY + minLine
                }
            }
            //绘制刻度
            canvas?.drawRoundRect(left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),(scaleWidth/2).toFloat(),
                (scaleWidth/2).toFloat(),paint)
            left += scaleWidth + intervalSize
        }
        //绘制一个刻度指示器
        canvas?.drawCircle(centerX.toFloat(),centerY-20f,scaleWidth/2f,paint)
        //绘制指示器的文字
        val indicatorWidth = paint.measureText(currScale.toString())
        canvas?.drawText(currScale.toString(),centerX-indicatorWidth/2,centerY-30f,paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                downX = event.x
                moved = 0
            }
            MotionEvent.ACTION_MOVE->{
                moved = (event.x - downX).toInt()
                if(abs(moved) > touchSlop){
                    //计算边界
                    if(offset + moved > centerX){
                        moved = 0
                        offset = centerX
                    }else if(offset + moved < centerX - totalWidth){
                        offset = centerX-totalWidth
                        moved = 0
                    }
                    currScale = (centerX-scaleWidth/2 - (offset + moved))/(scaleWidth+intervalSize)
                    Log.e("gt","downX:$downX  moved:$moved")
                    postInvalidate()
                }
            }
            MotionEvent.ACTION_UP->{
                offset += moved
                offset = ((offset/(scaleWidth+intervalSize))) * (scaleWidth+intervalSize) + (scaleWidth+intervalSize)/2
                moved = 0
                postInvalidate()
            }
        }
        return true
    }
}