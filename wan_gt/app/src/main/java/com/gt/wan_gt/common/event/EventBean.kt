package com.gt.wan_gt.common.event

import androidx.annotation.StringDef

/**
 * time 2020/8/4 0004
 * author GT
 * event bus事件
 */
data class EventBean(var type:Type,var data:Any?=null) {
    enum class Type{
        COLLECT,//收藏
        ORC_PATH//收藏
    }
}