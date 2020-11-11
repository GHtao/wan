package com.gt.wan_gt.search

import com.gt.wan_gt.db.DbManager
import com.gt.wan_gt.db.bean.SearchHistoryDb

/**
 * time 2020/7/24 0024
 * author GT
 * 数据处理
 */
class SearchFragmentModel {
    private val historyBox = DbManager.getSearchHistoryBox()

    private val MAX_SIZE = 10

    /**
     * 添加数据
     */
    fun addHistory(historyDb: SearchHistoryDb):Boolean{
        val all = historyBox.all
        all.forEach {
            if(it.data == historyDb.data){//数据库中存在相同的数据
                it.time = historyDb.time
                historyBox.put(it)
                return true
            }
        }
        if(all.size >= MAX_SIZE){//超过最大就删除最老的
            historyBox.remove(all.minBy { it.time }!!)
        }
        historyBox.put(historyDb)
        return true
    }

    /**
     * 获取所有数据
     */
    fun getAllData(): MutableList<SearchHistoryDb> {
        val all = historyBox.all
        all.sortByDescending { it.time }
        return all
    }

    /**
     * 清楚所有数据
     */
    fun clearData(){
        historyBox.removeAll()
    }
}