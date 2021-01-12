package cn.laitt.wanandroid.api

import cn.laitt.wanandroid.model.LoginBean
import com.emcrp.network.beans.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Login {
    /**
     * 登录
     * 方法： POST
     * 参数：
     * username，password
     * 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证。
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<LoginBean>

    /**
     * register
     * 方法： POST
     * 参数：
     * username，password，repassword
     *
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("repassword") repassword: String?
    ): Observable<LoginBean>

    /**
     * logout
     * 方法： GET
     * 参数：
     *
     *
     */
    @GET("user/logout/json")
    fun logout(): Observable<BaseResponse>

}