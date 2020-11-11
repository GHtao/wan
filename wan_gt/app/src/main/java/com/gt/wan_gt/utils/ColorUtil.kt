package com.gt.wan_gt.utils

import android.graphics.Color
import android.util.SparseArray
import android.util.SparseIntArray
import java.util.*

/**
 * time 2020/7/24 0024
 * author GT
 */
object ColorUtil {

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = random.nextInt(150)
        //0-190
        val green = random.nextInt(150)
        //0-190
        val blue = random.nextInt(150)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }
}