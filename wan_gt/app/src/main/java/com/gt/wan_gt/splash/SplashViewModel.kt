package com.gt.wan_gt.splash

import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 * time 2020/7/13 0013
 * author GT
 */
class SplashViewModel:BaseViewModel() {
    var jump = MutableLiveData<Boolean>()
    fun jumpToMain(){
        launchUI({
            withContext(Dispatchers.IO){
                delay(2000)
                jump.postValue(true)
            }
        },{

        })
    }
}