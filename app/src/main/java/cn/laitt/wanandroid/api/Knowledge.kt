package cn.laitt.wanandroid.api

import cn.laitt.wanandroid.model.*
import com.emcrp.network.beans.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface Knowledge {
    //    /tree/json
    @GET("/tree/json")
    fun getTree(): Observable<Tree>

    /**
     * 知识体下的文章
     */
    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int?, @Query("cid") cid: Int?): Observable<Article>


    /**
     * 导航
     */
    @GET("navi/json")
    fun getNvi(): Observable<Navigation>

    /**
     * 公众号
     */
    @GET("wxarticle/chapters/json")
    fun getWx(): Observable<Wx>

    /**
     * 公众号下的文章
     */
    @GET("wxarticle/list/{cid}/{page}/json")
    fun getWxArticle(
        @Path("page") page: Int?,
        @Path("cid") cid: Int?,
        @Query("k") k: String?
    ): Observable<WxArticel>

    /**
     * 收藏
     */
    @POST("lg/collect/{cid}/json")
    fun collect(@Path("cid") cid: Int): Observable<BaseResponse>

    /**
     * 收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    fun collectList(@Path("page") page: Int): Observable<Article>

    /**
     * 取消收藏 文章列表
     * 方法：POST
     * 参数：
     * id:拼接在链接上 id传入的是列表中文章的id。
     */
    @GET("lg/uncollect_originId/{id}/json")
    fun uncollectArticle(@Path("id") id: Int): Observable<BaseResponse>

    /**
     * 取消收藏 我的收藏页面（该页面包含自己录入的内容）
     * 方法：POST
     * 参数：
     * id:拼接在链接上
     * originId:列表页下发，无则为-1
     * originId 代表的是你收藏之前的那篇文章本身的id； 但是收藏支持主动添加，这种情况下，没有originId则为-1
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    fun uncollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): Observable<BaseResponse>


    /**
     * 收藏网站列表
     * 方法：GET
     */
    @GET("lg/collect/usertools/json")
    fun usertools(): Observable<BaseResponse>


    /**
     * 收藏网址
     * 方法：POST
     * 参数：
     * name,link
     */
    @FormUrlEncoded
    @POST("lg/collect/addtool/json")
    fun collectLink(
        @Field("name") name: String,
        @Field("link") link: String
    ): Observable<BaseResponse>

    /**
     * 删除收藏网站
     * 方法：POST
     * 参数：
     * id
     */
    @FormUrlEncoded
    @POST("lg/collect/deletetool/json")
    fun uncollectLink(@Field("id") id: Int): Observable<BaseResponse>
}