package com.gt.core.base

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException

/**
 * time 2020/5/25 0025
 * author GT
 */
open class BaseViewModel:ViewModel() {
    var isLoading = MutableLiveData<BaseResult<String>>()
    var result = MutableLiveData<BaseResult<String>>()

    /**
     * 取消任务
     */
    suspend fun cancelAndYield(job: CoroutineScope, msg:String = ""){
        job.cancel(msg)
        yield()
    }
    /**
     * 控制请求流程
     */
    fun launchUI(call:suspend ()->Unit,
                 error:suspend (ErrorResult)->Unit,
                 finally:suspend ()->Unit = {},
                 errorMsg:String=""){
        viewModelScope.launch {
            try{
                call()//当前线程调用
            }catch (e:Exception){
                e.printStackTrace()
                var msg = e.message!!
                var errorType = ErrorType.OTHER_ERROR
                if(!TextUtils.isEmpty(errorMsg)){
                    msg = errorMsg
                }else if(e is ConnectException){
                    msg = "连接失败,请检查网络"
                    errorType = ErrorType.NET_ERROR
                }else if(e is CancellationException){
                    msg = e.message.toString()
                    errorType = ErrorType.CANCEL_ERROR
                }
                error(ErrorResult(errorType,msg))
            }finally {
                finally()
            }
        }
    }

}