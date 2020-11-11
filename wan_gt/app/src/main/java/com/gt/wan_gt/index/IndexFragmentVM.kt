package com.gt.wan_gt.index

import android.util.Log
import com.gt.core.base.BaseViewModel
import com.gt.core.base.BaseResult
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.common.WanSp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class IndexFragmentVM:BaseViewModel() {

    /**
     * 退出登录
     */
    fun logout(){
        launchUI({
            isLoading.postValue(BaseResult(true,"正在退出"))
            withContext(Dispatchers.IO){
                WanApi.wanService.logout().apply {
                    if(errorCode == 0){//退出成功
                        WanSp.setLoginAccount("")
                        WanSp.setLoginPassword("")
                        WanSp.setLoginState(false)
                        result.postValue(BaseResult(true))
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