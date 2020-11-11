package com.gt.wan_gt.web_view

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/8/4 0004
 * author GT
 */
class WebViewActivityVM:BaseViewModel() {

    val cancelResult = MutableLiveData<BaseResult<String>>()
    val collectResult = MutableLiveData<BaseResult<String>>()

    /**
     * 取消收藏
     */
    fun cancelCollect(id:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.unCollectArticle(id).apply {
                    if(errorCode == 0){
                        cancelResult.postValue(BaseResult(true))
                    }else{
                        cancelResult.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }

    /**
     * 添加收藏
     */
    fun addCollect(id:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.collectArticle(id).apply {
                    if(errorCode == 0){
                        collectResult.postValue(BaseResult(true))
                    }else{
                        collectResult.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }
}