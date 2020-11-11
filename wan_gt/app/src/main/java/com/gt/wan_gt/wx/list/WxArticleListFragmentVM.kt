package com.gt.wan_gt.wx.list

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.FeedArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/30 0030
 * author GT
 */
class WxArticleListFragmentVM:BaseViewModel() {
    val wxArticleList = MutableLiveData<ArrayList<FeedArticleBean.FeedArticleData>>()
    /**
     * 获取公众号列表数据
     */
    fun loadWxArticleList(id:Int,page:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.wxArticleListData(id,page).apply {
                    if(errorCode == 0){
                        wxArticleList.postValue(data?.datas)
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
     * 获取搜索的数据
     */
    fun getSearchData(id:Int,page:Int,k:String){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.wxArticleSearch(id,page,k).apply {
                    if(errorCode == 0){
                        wxArticleList.postValue(data?.datas)
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