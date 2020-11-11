package com.gt.wan_gt.common

import android.os.Environment
import java.io.File

/**
 * time 2020/7/15 0015
 * author GT
 * 一些目录的管理
 */
object PathManager {
    val EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().absolutePath
    val APP_DIR = EXTERNAL_STORAGE+ File.separator+"wan_app"
    val CACHE_DIR = APP_DIR+File.separator+"cache"//缓存目录
    val CRASH_LOG_DIR = APP_DIR+File.separator+"crash"//crash目录
    init {
        val file = File(APP_DIR)
        if(!file.exists()){
            file.mkdirs()
        }
    }

}