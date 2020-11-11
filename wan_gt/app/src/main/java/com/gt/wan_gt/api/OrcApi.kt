package com.gt.wan_gt.api

/**
 * time 2020/8/19 0019
 * author GT
 */
object OrcApi {
    private const val baseUrl = "https://tysbgpu.market.alicloudapi.com/"
    lateinit var orcService: IOrcService
    /**
     * 初始化api
     */
    fun initApi(){
        orcService = RetrofitManager.create(baseUrl,IOrcService::class.java)
    }
}