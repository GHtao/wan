package com.gt.wan_gt.project.list

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
class ProjectListFragmentVM:BaseViewModel() {
    val projectListData = MutableLiveData<ArrayList<FeedArticleBean.FeedArticleData>>()

    /**
     * 根据id获取列表数据
     */
    fun loadProjectListData(page:Int,cid:Int){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.projectListData(page,cid).apply {
                    if(errorCode == 0){
                        projectListData.postValue(data!!.datas)
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