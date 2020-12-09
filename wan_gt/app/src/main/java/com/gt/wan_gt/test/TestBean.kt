package com.gt.wan_gt.test

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.entity.SectionEntity

/**
 * time 2020/11/18 0018
 * author GT
 */
class TestBean : MultiItemEntity {
    var title = ""
    var image = 0

    var isAd = false
    override val itemType: Int
        get() = if(isAd) AD else NORMAl

    companion object{
        const val NORMAl = 0
        const val AD = 1
    }

}