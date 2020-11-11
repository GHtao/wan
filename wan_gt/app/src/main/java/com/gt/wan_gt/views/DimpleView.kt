package com.gt.wan_gt.views

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.gt.wan_gt.R
import java.util.*
import java.util.concurrent.FutureTask
import kotlin.concurrent.thread
import kotlin.math.*


/**
 * time 2020/10/20 0020
 * author GT
 * 向外扩散的粒子
 */
class DimpleView @JvmOverloads constructor(context:Context,
                                           attr:AttributeSet?=null,
                                           style:Int=0):View(context,attr,style) {
    private val paint = Paint()
    private val particles = mutableListOf<Particle>()
    private var centerX = 0f
    private var centerY= 0f
    private val random = Random()
    private val path = Path()
    //可以获取圆形的x y位置
    private val pathMeasure = PathMeasure()
    //位置数组pos[0] x位置 pos[1] y位置
    private val pos = FloatArray(2)
    //切线位置
    private val tan = FloatArray(2)
    //定义一个动画
    private val translateAnim = ObjectAnimator.ofFloat(0f,1f)
    private val circleRadius = 200f//圆的半径
    private val randomSpeed = 4
    init {
        translateAnim.apply {
            duration = 2000
            repeatCount = -1
            interpolator = LinearInterpolator()
            addUpdateListener {
//                Log.e("gt","anim value:${it.animatedValue}")
                updateParticles(it.animatedValue as Float)
                invalidate()
            }
        }
    }

    /**
     * 根据动画 更新粒子
     */
    private fun updateParticles(value:Float){
        particles.forEach {
            if(it.offset > it.maxOffset){//最大距离的限制
                it.offset = 0f
                it.speed = (random.nextInt(randomSpeed)+2).toFloat()
            }
            //设置透明度
            it.alpha = ((1f- it.offset/it.maxOffset)*225).toInt()
            //计算x方向移动的距离
            it.x = centerX + cos(it.angle)*(it.offset+circleRadius)
            //计算y方向移动的距离
            if(it.y > centerY){
                it.y = sin(it.angle)*(it.offset+circleRadius)+centerY
            }else{
                it.y = centerY - sin(it.angle)*(it.offset+circleRadius)
            }
            it.offset += it.speed
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w/2f
        centerY = h/2f
        particles.clear()
        //定义出一个圆形
        path.addCircle(centerX,centerY,circleRadius,Path.Direction.CCW)
        pathMeasure.setPath(path,false)
        Log.e("gt","w:$w  h:$h")
        val size  = 300
        for(i in 0..size){
            //按比例测量路径上每一点的坐标值
            pathMeasure.getPosTan(i*pathMeasure.length/size,pos,tan)
            val particle = Particle().apply {
                x = pos[0]+(random.nextInt(20)-10).toFloat()//添加一个随机的偏移量
                y = pos[1]+(random.nextInt(20)-10).toFloat()
                angle = acos((pos[0] - centerX)/circleRadius)//记录这个点对于圆心的cos值
                Log.e("gt","x:$x  y:$y")
                radius = 2f
                alpha = 255
                //移动速度也随机
                speed = (random.nextInt(randomSpeed)+2).toFloat()
            }
            particles.add(particle)
        }
//        translateAnim.start()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = resources.getColor(R.color.white)
        particles.forEach {particle->
            paint.alpha = particle.alpha
            canvas?.drawCircle(particle.x,particle.y,particle.radius,paint)
        }
    }

    fun startDimple(){
        translateAnim.start()
    }
    fun stopDimple(){
        translateAnim.cancel()
    }
    /**
     * 粒子类
     */
    class Particle{
        var x = 0f//x方向位置绘制位置
        var y = 0f//y方向位置
        var radius = 0f//半径
        var alpha = 0//透明度
        var speed = 0f//速度
        var maxOffset = 100f//最大的移动距离
        var offset = 0f//偏移位置
        var angle = 0f//角度
    }
}