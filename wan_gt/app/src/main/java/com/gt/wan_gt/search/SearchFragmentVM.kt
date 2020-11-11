package com.gt.wan_gt.search

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.HotSearchBean
import com.gt.wan_gt.db.bean.SearchHistoryDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/23 0023
 * author GT
 */
class SearchFragmentVM:BaseViewModel() {
    val hotSearchData = MutableLiveData<List<HotSearchBean>>()
    private val model = SearchFragmentModel()
    val historyData = MutableLiveData<ArrayList<SearchHistoryDb>>()
    /**
     * 获取热搜和历史数据
     */
    fun loadData(){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.getHotSearchData().apply {
                    if(errorCode == 0){
                        hotSearchData.postValue(this.data)
                    }else{
                        result.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }

    /**
     * 获取所有历史数据
     */
    fun allHistoryData(): MutableList<SearchHistoryDb>{
        return model.getAllData().apply {
            if(this.size > 0){
                historyData.postValue(this as ArrayList<SearchHistoryDb>)
            }
        }
    }
    /**
     * 将搜索过的数据加入数据库
     */
    fun addHistory(content:String){
        val historyDb = SearchHistoryDb(data = content,time = System.currentTimeMillis())
        model.addHistory(historyDb)
    }

    /**
     * 清除数据
     */
    fun clearHistory(){
        model.clearData()
    }
}