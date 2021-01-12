package com.gt.wan_gt.home

import android.text.TextUtils
import android.util.Log
import android.view.TextureView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.BannerBean
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.common.WanMMKV
import com.gt.wan_gt.common.WanSp
import com.youth.banner.Banner
import kotlinx.coroutines.*

/**
 * time 2020/7/13 0013
 * author GT
 */
open class HomeFragmentVM:BaseViewModel() {
    var bannerData = MutableLiveData<BaseResult<List<BannerBean>>>()
    var articleData = MutableLiveData<BaseResult<FeedArticleBean>>()
    var collectResult = MutableLiveData<BaseResult<Int>>()
    var unCollectResult = MutableLiveData<BaseResult<Int>>()
    var currPage = 0
    /**
     * 获取home页面需要的数据
     */
    fun loadData(){
        val loginAccount = WanMMKV.getLoginAccount()
        val loginPassword = WanMMKV.getLoginPassword()
        launchUI({
            withContext(Dispatchers.IO){
                if(!TextUtils.isEmpty(loginAccount) && !TextUtils.isEmpty(loginPassword)) {
                    async {
                        WanApi.wanService.login(loginAccount, loginPassword)
                    }
                }
                async {
                    WanApi.wanService.getBannerData().apply {
                        if(errorCode == 0){
                            bannerData.postValue(BaseResult(true,data))
                        }else{
                            bannerData.postValue(BaseResult(false))
                        }
                    }
                }
                async {
                    loadArticle(0)
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }

    /**
     * 加载更多
     */
    fun loadMode(){
        launchUI({
            currPage++
            loadArticle(currPage)
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }
    /**
     * 分页加载数据
     */
    private suspend fun loadArticle(page:Int){
        withContext(Dispatchers.IO){
            WanApi.wanService.getArticleLise(page).apply {
                if(errorCode == 0){
                    articleData.postValue(BaseResult(true,data))
                }else{
                    articleData.postValue(BaseResult(false))
                }
            }
        }
    }
    /**
     * 收藏文章
     */
    fun collectArticle(position:Int,bean:FeedArticleBean.FeedArticleData){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.collectArticle(bean.id).apply {
                    if(errorCode == 0){
                        collectResult.postValue(BaseResult(true,position))
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
     * 取消收藏
     */
    fun cancelCollectArticle(position:Int,bean:FeedArticleBean.FeedArticleData){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.unCollectArticle(bean.id).apply {
                    if(errorCode == 0){
                        unCollectResult.postValue(BaseResult(true,position))
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