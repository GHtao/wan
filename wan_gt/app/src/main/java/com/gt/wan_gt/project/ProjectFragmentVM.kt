package com.gt.wan_gt.project

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.ProjectBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class ProjectFragmentVM:BaseViewModel() {
    var projectData = MutableLiveData<ArrayList<ProjectBean>>()
    /**
     * 加载项目数据
     */
    fun loadData(){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.projectData().apply {
                    if(errorCode == 0){
                        projectData.postValue(data)
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