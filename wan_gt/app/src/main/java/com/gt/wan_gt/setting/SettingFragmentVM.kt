package com.gt.wan_gt.setting

import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.common.PathManager
import com.gt.wan_gt.common.WanSp
import com.gt.wan_gt.utils.ACache
import java.io.File

/**
 * time 2020/8/3 0003
 * author GT
 */
class SettingFragmentVM:BaseViewModel() {

    /**
     * 获取初始状态
     */
    fun getCacheState():Boolean{
        return WanSp.getAutoCache()
    }

    fun setCacheState(state:Boolean){
        WanSp.setAutoCache(state)
    }
    /**
     * 获取初始状态
     */
    fun getImageState():Boolean{
        return WanSp.getNoImage()
    }

    fun setImageState(state:Boolean){
        WanSp.setNoImage(state)
    }
    /**
     * 获取初始状态
     */
    fun getNightState():Boolean{
        return WanSp.getNightMode()
    }

    fun setNightState(state:Boolean){
        WanSp.setNightMode(state)
    }

    /**
     * 清除缓存
     */
    fun clearCache(){
        val cacheFile = File(PathManager.CACHE_DIR)
        ACache.deleteDir(cacheFile)
    }

    /**
     * 获取缓存目录的大小
     */
    fun getCacheSize():String{
        val cacheFile = File(PathManager.CACHE_DIR)
        return ACache.getCacheSize(cacheFile)
    }
}