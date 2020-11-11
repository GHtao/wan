package com.gt.wan_gt.common

import android.content.Context
import android.content.SharedPreferences
import com.gt.wan_gt.WanApp

/**
 * time 2019/3/14 0014
 * author GT
 * 整体的sp对象
 */
object WanSp {

    private var preferences: SharedPreferences
    private const val FILE_NAME = "wan_preference"
    init {
        preferences = WanApp.instance!!
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    }

    @Synchronized
    private fun putString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    @Synchronized
    private fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    @Synchronized
    private fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    @Synchronized
    private fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    @Synchronized
    private fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue)!!
    }

    @Synchronized
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    @Synchronized
    private fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    @Synchronized
    private fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    /**
     * 登录账号
     */
    fun setLoginAccount(account:String){
        putString("login_account",account)
    }
    fun getLoginAccount():String{
        return getString("login_account","")
    }
    /**
     * 登录密码
     */
    fun setLoginPassword(password:String){
        putString("login_password",password)
    }
    fun getLoginPassword():String{
        return getString("login_password","")
    }
    /**
     * 登录状态
     */
    fun setLoginState(state:Boolean){
        putBoolean("login_state",state)
    }
    fun getLoginState():Boolean{
        return getBoolean("login_state",false)
    }

    /**
     * 自动缓存
     */
    fun setAutoCache(state:Boolean){
        putBoolean("auto_cache",state)
    }
    fun getAutoCache():Boolean{
        return getBoolean("auto_cache",true)
    }

    /**
     * 无图模式
     */
    fun setNoImage(state:Boolean){
        putBoolean("no_image",state)
    }
    fun getNoImage():Boolean{
        return getBoolean("no_image",false)
    }

    /**
     * 夜间模式
     */
    fun setNightMode(state:Boolean){
        putBoolean("night_mode",state)
    }
    fun getNightMode():Boolean{
        return getBoolean("night_mode",false)
    }


}