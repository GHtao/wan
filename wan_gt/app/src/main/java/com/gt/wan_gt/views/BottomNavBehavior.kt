package com.gt.wan_gt.views

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.viewpager2.widget.ViewPager2
import me.jessyan.autosize.utils.ScreenUtils
import kotlin.math.abs


/**
 * time 2020/7/31 0031
 * author GT
 */
class BottomNavBehavior @JvmOverloads constructor(val context:Context, attributeSet: AttributeSet?=null)
    :CoordinatorLayout.Behavior<View>(context,attributeSet) {

    private var touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var scrollFlag = false
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is ViewPager2
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View,
                                     directTargetChild: View,
        target: View,
        axes: Int,
        type: Int): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray, type: Int) {

        if(abs(dy) > touchSlop){
            val screenHeight = ScreenUtils.getScreenSize(context)[1]
            if(dy < 0){//下滑
                if(scrollFlag){
                    scrollFlag = false
                    //会获取之前移动过的距离 所以是0
//                    child.animate().translationY(0f).setDuration(200).start()
                    Log.e("gt","down top:${child.top}")
                    ObjectAnimator.ofFloat(child,
                        "translationY",(screenHeight - child.top).toFloat(),0f)
                        .setDuration(200).start()
                }
            }else{//上滑
                if(!scrollFlag){
                    scrollFlag = true
                    Log.e("gt","up top:${child.top}")
//                    child.animate().translationY(child.height.toFloat()).setDuration(200).start()
                    ObjectAnimator.ofFloat(child,"translationY",0f,(screenHeight - child.top).toFloat())
                        .setDuration(200).start()
                }
            }
        }
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View): Boolean {

        return true
    }
}