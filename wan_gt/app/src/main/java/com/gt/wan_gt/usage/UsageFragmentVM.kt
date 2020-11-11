package com.gt.wan_gt.usage

import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.BaseBean
import com.gt.wan_gt.api.bean.UsageBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/17 0017
 * author GT
 */
class UsageFragmentVM:BaseViewModel() {
    var usageData = MutableLiveData<BaseResult<List<UsageBean>>>()
    /**
     * 获取常用网站数据
     */
    fun loadData(){
        launchUI({
            isLoading.postValue(BaseResult(true))
            withContext(Dispatchers.IO){
                WanApi.wanService.getUsageData().apply {
                    if(errorCode == 0){
                        usageData.postValue(BaseResult(true,data))
                    }else{
                        result.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        },{
            isLoading.postValue(BaseResult(false))
        })
    }
}