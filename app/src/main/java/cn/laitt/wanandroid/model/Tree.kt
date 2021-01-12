package cn.laitt.wanandroid.model

import com.emcrp.network.beans.BaseResponse
import java.io.Serializable

class Tree : BaseResponse(), Serializable {
    var data: List<Data>? = null

    data class Data(
        val children: List<Children>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    ) : Serializable

    data class Children(
        val children: List<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
    ) : Serializable
}