package com.gt.wan_gt.login

import com.gt.core.base.BaseViewModel
import com.gt.core.base.BaseResult
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.common.WanMMKV
import com.gt.wan_gt.common.WanSp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class LoginActivityVM:BaseViewModel() {

    /**
     * 登录方法
     */
    fun login(name:String,password:String){
        launchUI({
            isLoading.value = BaseResult(true)
            withContext(Dispatchers.IO){
                WanApi.wanService.login(name,password).apply {
                    if(errorCode == 0){
                        WanMMKV.setLoginState(true)
                        WanMMKV.setLoginAccount(name)
                        WanMMKV.setLoginPassword(password)
                        isLoading.postValue(BaseResult(false))
                        result.postValue(BaseResult(true,"登录成功"))
                    }else{
                        isLoading.postValue(BaseResult(false))
                        result.postValue(BaseResult(false,errorMsg))
                    }
                }
            }
        },{
            isLoading.postValue(BaseResult(false))
            result.postValue(BaseResult(false,it.msg))
        })
    }
}