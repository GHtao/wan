package com.gt.wan_gt.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnStart

/**
 * time 2020/11/3 0003
 * author GT
 */
class WaveDisView @JvmOverloads constructor(context: Context,
                                            attributeSet: AttributeSet?=null, style:Int=0)
                                        :View(context,attributeSet,style) {

    private var centerX = 0
    private var centerY = 0
    private val paint = Paint()
    private val path = Path()
    private var currentY = 0
    private var dragWidth = 100
    private var dragHeight = 0

    //上半部分
    private val point1 = PointF(0f,0f)
    private val point2 = PointF(0f,0f)
    private val point3 = PointF(0f,0f)
    //最右边
    private val point4 = PointF(0f,0f)
    //下半部分
    private val point5 = PointF(0f,0f)
    private val point6 = PointF(0f,0f)
    private val point7 = PointF(0f,0f)

    private val animator = ValueAnimator.ofFloat(0f,1f)
    private var reboundLength = 0f //需要回弹的距离是多少
    private var dragReboundX = 0f // 初始回弹地点的X值
    init {
        animator.doOnStart {
            reboundLength = dragWidth - 100f
            dragReboundX = dragWidth.toFloat()
        }
        animator.duration = 1500
        animator.interpolator = OvershootInterpolator(3f)
        animator.addUpdateListener {
            currentY = centerY
            dragWidth = (dragReboundX - it.animatedValue as Float * reboundLength).toInt()
            invalidate()
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w/2
        centerY = h/2
        currentY = centerY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        canvas?.apply {
//            paint.color = Color.RED
//            paint.strokeWidth = 4f
//            paint.textSize = 24f
//            drawColor(Color.GRAY)
//            save()//保存的是上面的状态
//            rotate(90f,centerX.toFloat(),centerY.toFloat())
//            drawLine(centerX+100f,centerY.toFloat(),centerX.toFloat(),centerY-100f,paint)
//            drawLine(centerX+100f,centerY.toFloat(),centerX.toFloat(),centerY+100f,paint)
//            drawLine(centerX+100f,centerY.toFloat(),centerX-100f,centerY.toFloat(),paint)
//            restore()//恢复的是保存之前的状态 save之后的旋转 平移等操作在restore之后 下面的绘制不会受到影响
//            drawText("test",centerX.toFloat(),centerY-20f,paint)
//        }

        canvas?.apply {
            paint.color = Color.RED
            dragHeight = dragWidth*1.5f.toInt()

            point1.x=100f
            point1.y=currentY-dragHeight.toFloat()

            point2.x=100f
            point2.y=(currentY+point1.y)/2 + 30

            point3.x=(100f+dragWidth)*0.94f
            point3.y=(point1.y+currentY)/2

            point4.x=100f+dragWidth
            point4.y=currentY.toFloat()

            point7.x=100f
            point7.y=currentY+dragHeight.toFloat()

            point5.x=(100f+dragWidth)*0.94f
            point5.y=(currentY+point7.y)/2

            point6.x=100f
            point6.y=currentY+dragHeight/2 - 30f

            //第一步
            path.lineTo(100f,0f)
            //第二步
            path.lineTo(point1.x,point1.y)
            //第三步，画上半部分
            path.cubicTo(point2.x,point2.y,point3.x,point3.y,point4.x,point4.y)
            //第四步，画下半部分
            path.cubicTo(point5.x,point5.y,point6.x,point6.y,point7.x,point7.y)
            //第五步
            path.lineTo(100f,centerY*2f)
            //第六步
            path.lineTo(0f,centerY*2f)
            //第七步
            path.close()//代替0，0点
            drawPath(path,paint)
            path.reset()//每次花完之后 清除之前的路径
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action){
            MotionEvent.ACTION_MOVE->{
                //上下移动
                currentY = event.y.toInt()
                //控制不能划过屏幕中心
                dragWidth = if(event.x > centerX){
                    centerX - 30
                }else{
                    (event.x - 30).toInt()
                }
                invalidate()
            }
            MotionEvent.ACTION_UP->{
                //没有过中心 就回到原点
                if(dragWidth < centerX){
                    animator.start()
                }
            }
        }
        return true

    }
}