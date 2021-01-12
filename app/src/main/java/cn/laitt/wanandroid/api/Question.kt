package cn.laitt.wanandroid.api

import cn.laitt.wanandroid.model.Article
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Question {
    /**
     * 问答文章
     */
    @GET("wenda/list/{page}/json")
    fun getArticle(@Path("page") page: Int?): Observable<Article>
}