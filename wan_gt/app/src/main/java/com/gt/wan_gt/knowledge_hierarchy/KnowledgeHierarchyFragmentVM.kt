package com.gt.wan_gt.knowledge_hierarchy

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.KnowledgeBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class KnowledgeHierarchyFragmentVM:BaseViewModel() {
    val knowledgeData = MutableLiveData<ArrayList<KnowledgeBean>>()
    /**
     * 获取数据
     */
    fun loadData(){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.knowledgeData().apply {
                    if(errorCode == 0){
                        knowledgeData.postValue(data as ArrayList<KnowledgeBean>)
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