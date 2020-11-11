package com.gt.wan_gt.orc

import android.util.ArrayMap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gt.core.base.BaseResult
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.api.OrcApi
import com.gt.wan_gt.api.body.OrcBody
import com.gt.wan_gt.common.PathManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Buffer
import okio.Okio
import java.io.File
import java.lang.StringBuilder
import kotlin.math.log

/**
 * time 2020/8/19 0019
 * author GT
 */
class OrcFragmentVM:BaseViewModel() {
    private val arrayMap = ArrayMap<String,String>()
    val orcResult = MutableLiveData<String>()
    private val resultBuffer = StringBuilder()
    init {
        arrayMap["Authorization"] = "APPCODE abab920305e64f1dbe6a63d9bee5dcc3"
        arrayMap["Content-Type"] = "application/json; charset=UTF-8"
    }
    /**
     * 识别orc
     */
    fun loadOrc(path:String){
        launchUI({
            withContext(Dispatchers.IO){
                val orcBody = OrcBody()
                val source = Okio.buffer(Okio.source(File(path)))
                orcBody.image = Base64.encodeToString(source.readByteArray(),Base64.DEFAULT)
                source.close()
                orcBody.configure = OrcBody.Configure()
                OrcApi.orcService.orcApi(arrayMap,orcBody).apply {
                    if(this.success){
                        resultBuffer.clear()
                        this.ret!!.forEach{
                            Log.e("gt",it.word)
                            resultBuffer.append(it.word)
                            resultBuffer.append("  ")
                        }
                        orcResult.postValue(resultBuffer.toString())
                    }else{
                        result.postValue(BaseResult(false,"解析失败"))
                    }
                }
            }
        },{
            result.postValue(BaseResult(false,it.msg))
        })
    }
}