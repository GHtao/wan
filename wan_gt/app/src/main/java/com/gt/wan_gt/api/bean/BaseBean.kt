package com.gt.wan_gt.api.bean

/**
 * time 2020/7/14 0014
 * author GT
 */
class BaseBean<T> {
    var errorMsg:String = ""
    //errorCode = -1001 代表登录失效，需要重新登录
    var errorCode:Int = -1//0 成功 非0失败
    var data:T? = null
}