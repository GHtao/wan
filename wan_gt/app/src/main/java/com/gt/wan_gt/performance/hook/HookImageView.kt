package com.gt.wan_gt.performance.hook

import android.graphics.Bitmap
import android.util.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect

/**
 * time 2020/8/12 0012
 * author GT
 * Aspect hook图片的加载
 * 与多个dex文件冲突 导致class not found问题
 *
 * 一个空的@Aspect可以防止class not found异常
 */
@Aspect
class HookImageView {
    /**
     * 第一个*所在的位置表示的是返回值，*表示的是任意的返回值，
     * setImageBitmap()中的 .. 所在位置是方法参数的位置，.. 表示的是任意类型、任意个数的参数
     *   *表示的是通配
     *   after 方法执行之后
     *   before 方法执行之前 需要调用 joinPoint.proceed();
     *   around 方法执行前后 需要调用 joinPoint.proceed();
     */
//    @After("execution(* android.widget.ImageView.setImageBitmap(..))")
    fun hookImageView(joinPoint: ProceedingJoinPoint){
//        val params = joinPoint.args
//        val bitmap = params[0] as Bitmap
//        Log.e("gt","target:${joinPoint.target}")
//        Log.e("gt","bitmap:${bitmap}")
//        DexposedBridge.hookAllConstructors(Thread::class.java,object :XC_MethodHook(){
//            override fun afterHookedMethod(param: MethodHookParam?) {
//                super.afterHookedMethod(param)
//                Log.e("gt","hook imageView Constructors")
//                DexposedBridge.findAndHookMethod(ImageView::class.java,
//                    "setImageBitmap",
//                    Bitmap::class.java,
//                    object :XC_MethodHook(){
//                        override fun afterHookedMethod(param: MethodHookParam?) {
//                            super.afterHookedMethod(param)
//                            val imageView = param?.thisObject as ImageView
//                            val drawable = imageView.drawable
//                            if(drawable is BitmapDrawable){
//                                val bitmap = drawable.bitmap
//                                val width = imageView.width
//                                val height = imageView.height
//                                if(width>0 && height>0){//image view的宽高大于0
//                                    if(bitmap.width > width.shr(1) && bitmap.height > height.shr(1)){
//                                        //图片大小大于控件大小的2倍
//                                        Log.e("gt","bitmap width:${bitmap.width} ImageView width:${imageView.width}")
//                                        Log.e("gt","bitmap height:${bitmap.height} ImageView height:${imageView.height}")
//                                    }
//                                }else{
//                                    //image view 没有宽高 说明控件还没有初始化
//                                    imageView.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
//                                        override fun onPreDraw(): Boolean {
//                                            if(imageView.width>0 && imageView.height>0){
//                                                if(bitmap.width>imageView.width.shr(1)
//                                                    && bitmap.height>imageView.height.shr(1)){
//                                                    //图片大小大于 控件大小的2倍
//                                                    Log.e("gt","pre draw bitmap width:${bitmap.width} ImageView width:${imageView.width}")
//                                                    Log.e("gt","pre draw bitmap height:${bitmap.height} ImageView height:${imageView.height}")
//
//                                                }
//                                            }
//                                            imageView.viewTreeObserver.removeOnPreDrawListener(this)
//                                            return true
//                                        }
//
//                                    })
//                                }
//                            }
//                        }
//                })
//            }
//        })
    }
}