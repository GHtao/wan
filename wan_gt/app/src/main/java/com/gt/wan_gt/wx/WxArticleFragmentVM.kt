package com.gt.wan_gt.wx

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.WxArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/30 0030
 * author GT
 */
class WxArticleFragmentVM:BaseViewModel() {
    val wxArticleData = MutableLiveData<ArrayList<WxArticleBean>>()
    /**
     * 加载公众号数据
     */
    fun loaWxdData(){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.wxArticleData().apply {
                    if(errorCode == 0){
                        wxArticleData.postValue(data)
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