package cn.laitt.wanandroid.api

import cn.laitt.wanandroid.model.Article
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Project {
    @GET("project/tree/json")
    fun getProjectTree(): Observable<cn.laitt.wanandroid.model.Project>

    @GET("project/list/{page}/json")
    fun getArticle(@Path("page") page: Int?, @Query("cid") cid: Int?): Observable<Article>
}