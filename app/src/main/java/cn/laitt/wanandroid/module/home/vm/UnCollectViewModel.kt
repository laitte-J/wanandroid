package cn.laitt.wanandroid.module.home.vm

import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.beans.BaseResponse

class UnCollectViewModel : MvvmBaseViewModel<UnCollectModel, BaseResponse>() {
    fun init(): UnCollectViewModel {
        model = UnCollectModel()
        model.register(this)
        return this
    }

    fun uncollectArticle(cid: Int) {
        model.uncollectArticle(cid)
    }

    fun uncollectArticle(cid: Int, originId: Int) {
        model.uncollectArticle(cid, originId)
    }
}