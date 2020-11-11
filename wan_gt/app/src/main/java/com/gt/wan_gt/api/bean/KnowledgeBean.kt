package com.gt.wan_gt.api.bean

import java.io.Serializable

/**
 * time 2020/7/24 0024
 * author GT
 */
class KnowledgeBean :Serializable{
    var children: List<KnowledgeBean>? = null
    var courseId = 0
    var id = 0
    var name: String? = null
    var order = 0
    var parentChapterId = 0
    var visible = 0
}