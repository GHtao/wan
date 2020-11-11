package com.gt.wan_gt.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * time 2020/7/22 0022
 * author GT
 */
class MyFlowLayout @JvmOverloads constructor(context: Context,
                                             attributeSet: AttributeSet?=null,
                                             style:Int=0)
    :ViewGroup(context,attributeSet,style) {

    init{
        //修改子view的绘制顺序 dispatchTouchEvent顺序也会相应改变
        //复写getChildDrawingOrder
        isChildrenDrawingOrderEnabled = true
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        //可以根据需求来返回需要的顺序
        return super.getChildDrawingOrder(childCount, i)
    }
    private val horizontal = 8
    private val vertical = 8
    private var lineViews = ArrayList<View>()
    private var lineHeights = ArrayList<Int>()
    private val allViews = ArrayList<ArrayList<View>>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        lineViews.clear()
        lineHeights.clear()
        allViews.clear()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if(widthMode == MeasureSpec.EXACTLY){
            widthSize = layoutParams.width
        }
        Log.e("gt", "width:$widthSize")
        Log.e("gt", "width mode:${widthMode==MeasureSpec.EXACTLY}")
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var lineWidth = 0
        var lineHeight = 0

        var maxWidth = 0
        var maxHeight = 0
        //测量child
        for(i in 0 until childCount){
            val child = getChildAt(i)
            val layoutParams = child.layoutParams

            val childWidthSpec= getChildMeasureSpec(widthMeasureSpec,paddingLeft+paddingRight,layoutParams.width)
            val childHeightSpec = getChildMeasureSpec(heightMeasureSpec,paddingTop+paddingBottom,layoutParams.height)
            child.measure(childWidthSpec,childHeightSpec)

            val measuredWidth = child.measuredWidth
            val measuredHeight = child.measuredHeight
            if(lineWidth+measuredWidth+horizontal > widthSize){//判断当前行宽度是不是大于了父控件宽度
                maxWidth = maxWidth.coerceAtLeast(lineWidth)
                maxHeight += lineHeight
                allViews.add(lineViews)//记录每行view
                lineHeights.add(lineHeight)//记录每行的高度
                lineViews = ArrayList()
                lineWidth = 0
                lineHeight = 0
            }
            lineWidth += measuredWidth+horizontal
            lineViews.add(child)
            lineHeight = lineHeight.coerceAtLeast(measuredHeight+vertical)

            if(i == childCount - 1){
                allViews.add(lineViews)
                lineHeights.add(lineHeight)
            }

        }
        //测量自己
        val selfWidth = if(widthMode == MeasureSpec.EXACTLY) widthSize else maxWidth
        val selfHeight = if(heightMode == MeasureSpec.EXACTLY) heightSize else maxHeight
        setMeasuredDimension(selfWidth,selfHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top =0
        lineHeights.forEachIndexed { index, height ->
            val lineView = allViews[index]//每行的view
            var left =0
            lineView.forEach {
                //layout结束之后才能拿到width (right-left)
                val childWidth = it.measuredWidth
                val childHeight = it.measuredHeight
                it.layout(left,top,left+childWidth+horizontal,top+childHeight)
                left += childWidth + horizontal
            }
            top += height+vertical
        }
    }
}