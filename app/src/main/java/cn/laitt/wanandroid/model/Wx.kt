package cn.laitt.wanandroid.model

import com.emcrp.network.beans.BaseResponse

data class Wx(
    val data: List<Data>
) : BaseResponse() {
    data class Data(
        val children: List<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    )

}


