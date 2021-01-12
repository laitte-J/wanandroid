package cn.laitt.wanandroid.api

import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.model.Hotkey
import cn.laitt.wanandroid.model.TopArticle
import com.emcrp.network.beans.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface Home {
    /**
     * 首页banner
     */
    @GET("banner/json")
    fun getBanner(): Observable<Banner>

    /**
     * 热门文章
     */
    @GET("article/top/json")
    fun getTopArticle(): Observable<TopArticle>

    /**
     * 首页文章
     */
    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page: Int?): Observable<Article>

    /**
     * hotkey
     */
    @GET("hotkey/json")
    fun getHotKey(): Observable<Hotkey>

    /**
     * 搜索
     * 方法：POST
     * 参数：
     * 页码：拼接在链接上，从0开始。
     * k ： 搜索关键词
     * 支持多个关键词，用空格隔开
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun search(@Path("page") page: Int, @Field("k") key: String): Observable<Article>



}