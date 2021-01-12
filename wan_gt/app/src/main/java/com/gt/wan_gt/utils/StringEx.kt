package com.gt.wan_gt.utils

/**
 * time 2020/12/31 0031
 * author GT
 */
//扩展函数
fun String.add(a:String,b:String):String{
    return a+b
}
//扩展属性
var String.isEx:Boolean
    get() {
        return false
    }
    set(value) {

    }

class StringEx {
    fun test(){
        "".add("","")
        "".isEx = true
    }
}