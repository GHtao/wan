package com.gt.wan_gt.knowledge_detail.list

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.FeedArticleBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/28 0028
 * author GT
 */
class KnowledgeDetailListFragmentVM:BaseViewModel() {
    val listData = MutableLiveData<ArrayList<FeedArticleBean.FeedArticleData>>()
    /**
     * 获取数据
     */
    fun loadData(page:Int,cid:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.knowledgeListData(page,cid).apply {
                    if(errorCode == 0){
                        listData.postValue(data!!.datas!!)
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