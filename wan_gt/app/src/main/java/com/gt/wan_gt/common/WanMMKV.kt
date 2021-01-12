package com.gt.wan_gt.common

import com.tencent.mmkv.MMKV

/**
 * time 2020/10/13 0013
 * author GT
 */
object WanMMKV {
    private val mmkv = MMKV.defaultMMKV()
    //可以跨进程使用
    private val mmkvUser = MMKV.mmkvWithID("user",MMKV.MULTI_PROCESS_MODE)

    /**
     * 登录账号
     */
    fun setLoginAccount(account:String){
        mmkv.putString("login_account", account)
    }
    fun getLoginAccount():String{
        return mmkv.getString("login_account", "")!!
    }
    /**
     * 登录密码
     */
    fun setLoginPassword(password:String){
        mmkv.putString("login_password", password)
    }
    fun getLoginPassword():String{
        return mmkv.getString("login_password", "")!!
    }
    /**
     * 登录状态
     */
    fun setLoginState(state:Boolean){
        mmkvUser.putBoolean("login_state", state)
    }
    fun getLoginState():Boolean{
        return mmkvUser.getBoolean("login_state", false)
    }

    /**
     * 自动缓存
     */
    fun setAutoCache(state:Boolean){
        mmkv.putBoolean("auto_cache", state)
    }
    fun getAutoCache():Boolean{
        return mmkv.getBoolean("auto_cache", true)
    }

    /**
     * 无图模式
     */
    fun setNoImage(state:Boolean){
        mmkv.putBoolean("no_image", state)
    }
    fun getNoImage():Boolean{
        return mmkv.getBoolean("no_image", false)
    }

    /**
     * 夜间模式
     */
    fun setNightMode(state:Boolean){
        mmkv.putBoolean("night_mode", state)
    }
    fun getNightMode():Boolean{
        return mmkv.getBoolean("night_mode", false)
    }

    fun setIsFirstRun(isFirst:Boolean){
        mmkv.putBoolean("is_first",isFirst)
    }
    fun getIsFirstRun():Boolean{
        return mmkv.getBoolean("is_first",true)
    }
}