package com.gt.wan_gt.api.bean

/**
 * time 2020/7/15 0015
 * author GT
 */
class LoginBean {
    var username: String? = null//用户名
    var password: String? = null//密码
    var icon: String? = null//图标
    var type = 0//类型
    var collectIds: List<Int>? = null//收藏的id
}