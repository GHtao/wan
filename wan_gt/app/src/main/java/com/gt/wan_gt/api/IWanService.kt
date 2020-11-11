package com.gt.wan_gt.api

import com.gt.wan_gt.api.bean.*
import retrofit2.http.*

/**
 * time 2020/7/14 0014
 * author GT
 * 接口
 */
interface IWanService {
    /**
     * 注册接口
     */
    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username:String,
                 @Field("password") password:String,
                 @Field("repassword") rePassword:String):BaseBean<RegisterBean>

    /**
     * 登录接口
     */
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") name:String,
                      @Field("password") password: String):BaseBean<LoginBean>

    /**
     * 退出登录
     */
    @GET("user/logout/json")
    suspend fun logout():BaseBean<LoginBean>

    /**
     * 获取文章列表
     * num 页数
     */
    @GET("article/list/{num}/json")
    suspend fun getArticleLise(@Path("num")num:Int):BaseBean<FeedArticleBean>

    /**
     * 广告数据
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBannerData():BaseBean<List<BannerBean>>

    /**
     * 获取常用网站
     */
    @GET("friend/json")
    suspend fun getUsageData():BaseBean<List<UsageBean>>

    /**
     * 收藏文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(@Path("id")id:Int):BaseBean<FeedArticleBean>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticle(@Path("id")id:Int):BaseBean<FeedArticleBean>

    /**
     * 热搜数据
     */
    @GET("hotkey/json")
    @Headers("Cache-Control: public, max-age=36000")
    suspend fun getHotSearchData():BaseBean<List<HotSearchBean>>

    /**
     * 分页搜索文章
     * k 搜索的关键字
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun searchData(@Path("page") page:Int,@Field("k") k:String):BaseBean<FeedArticleBean>

    /**
     * 知识体系数据
     */
    @GET("tree/json")
    suspend fun knowledgeData():BaseBean<List<KnowledgeBean>>

    /**
     * 获取知识体系下的分页数据
     */
    @GET("article/list/{page}/json")
    suspend fun knowledgeListData(@Path("page")page:Int,@Query("cid")cid:Int):BaseBean<FeedArticleBean>

    /**
     * 获取导航页数据
     */
    @GET("navi/json")
    suspend fun navigationData():BaseBean<List<NavigationBean>>

    /**
     * 获取项目主页数据
     */
    @GET("project/tree/json")
    suspend fun projectData():BaseBean<ArrayList<ProjectBean>>

    /**
     * 获取项目列表数据
     * cid分类id
     */
    @GET("project/list/{page}/json")
    suspend fun projectListData(@Path("page")page:Int,@Query("cid")cid:Int):BaseBean<FeedArticleBean>

    /**
     * 获取公众号列表数据
     */
    @GET("wxarticle/chapters/json")
    suspend fun wxArticleData():BaseBean<ArrayList<WxArticleBean>>

    /**
     * 获取列表的具体数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun wxArticleListData(@Path("id")id:Int,@Path("page")page:Int):BaseBean<FeedArticleBean>

    /**
     * 在一个公众号下搜索
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun wxArticleSearch(@Path("id")id:Int,@Path("page")page:Int,@Query("k")k:String):BaseBean<FeedArticleBean>

    /**
     * 获取收藏数据
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectData(@Path("page")page:Int):BaseBean<FeedArticleBean>

}