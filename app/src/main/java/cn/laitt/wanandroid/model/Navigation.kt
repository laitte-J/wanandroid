package cn.laitt.wanandroid.model

import com.emcrp.network.beans.BaseResponse

data class Navigation(
    val data: List<Data>
) : BaseResponse()

data class Data(
    val articles: List<Article.DataBean.DatasBean>,
    val cid: Int,
    val name: String
) {
    var select: Boolean? = false
}

