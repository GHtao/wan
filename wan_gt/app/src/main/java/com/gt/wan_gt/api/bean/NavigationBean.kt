package com.gt.wan_gt.api.bean

/**
 * time 2020/7/28 0028
 * author GT
 * 导航页的bean
 */
class NavigationBean {
    var articles: List<FeedArticleBean.FeedArticleData>? = null
    var cid = 0
    var name: String? = null
    var isSelected = false//是否被选中
}