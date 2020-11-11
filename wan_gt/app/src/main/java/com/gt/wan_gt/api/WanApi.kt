package com.gt.wan_gt.api


/**
 * time 2020/7/14 0014
 * author GT
 */
object WanApi {
    private const val baseUrl = "https://www.wanandroid.com/"
    lateinit var wanService: IWanService
    /**
     * 初始化api
     */
    fun initApi(){
        wanService = RetrofitManager.create(baseUrl,IWanService::class.java)
    }

}