package com.gt.wan_gt.db

import android.content.Context
import com.gt.wan_gt.BuildConfig
import com.gt.wan_gt.db.bean.MyObjectBox
import com.gt.wan_gt.db.bean.SearchHistoryDb
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

/**
 * time 2020/7/13 0013
 * author GT
 * 数据库管理类
 */
object DbManager {
    private lateinit var boxStore:BoxStore
    fun initDb(context: Context){
        //MyObjectBox 创建实体类之后再编译就不会报错了
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
        if(BuildConfig.DEBUG){//开启浏览器预览
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }

    /**
     * 获取搜索历史的box
     */
    fun getSearchHistoryBox(): Box<SearchHistoryDb> {
        return boxStore.boxFor(SearchHistoryDb::class.java)
    }
}