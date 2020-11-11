package com.gt.wan_gt.register

import com.gt.core.base.BaseViewModel
import com.gt.core.base.BaseResult
import com.gt.wan_gt.api.WanApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class RegisterFragmentVM:BaseViewModel() {
    /**
     * 注册
     */
    fun register(account:String,password:String,rePassword:String){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.register(account,password,rePassword).apply {
                    if(this.errorCode == 0){
                        result.postValue(BaseResult(true))
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
