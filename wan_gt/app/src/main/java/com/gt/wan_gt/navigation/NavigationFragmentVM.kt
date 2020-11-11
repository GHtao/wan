package com.gt.wan_gt.navigation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseFragment
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.WanApi
import com.gt.wan_gt.api.bean.NavigationBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * time 2020/7/14 0014
 * author GT
 */
class NavigationFragmentVM:BaseViewModel() {
    val navigationData = MutableLiveData<ArrayList<NavigationBean>>()
    /**
     * 加载导航的数据
     */
    fun loadData(){
        launchUI({
            withContext(Dispatchers.IO){
                WanApi.wanService.navigationData().apply {
                    if(errorCode == 0){
                        navigationData.postValue(data as ArrayList<NavigationBean>)
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