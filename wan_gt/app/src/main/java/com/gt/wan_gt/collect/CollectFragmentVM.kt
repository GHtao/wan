package com.gt.wan_gt.collect

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.FeedArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/8/3 0003
 * author GT
 */
class CollectFragmentVM:BaseViewModel() {
    val collectData = MutableLiveData<ArrayList<FeedArticleBean.FeedArticleData>>()

    /**
     * 获取收藏数据
     */
    fun getCollectData(page:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.getCollectData(page).apply {
                    if(errorCode == 0){
                        collectData.postValue(data!!.datas)
                    }else{
                        result.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }
}