package com.gt.wan_gt.api

import android.util.ArrayMap
import com.gt.wan_gt.api.bean.BaseBean
import com.gt.wan_gt.api.bean.OrcBean
import com.gt.wan_gt.api.body.OrcBody
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

/**
 * time 2020/8/19 0019
 * author GT
 */
interface IOrcService {

    @POST("api/predict/ocr_general")
    suspend fun orcApi(@HeaderMap headerMap: ArrayMap<String,String>,
                       @Body orcBody: OrcBody):OrcBean
}