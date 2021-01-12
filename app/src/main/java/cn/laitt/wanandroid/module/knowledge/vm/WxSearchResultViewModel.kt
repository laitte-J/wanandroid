package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.WxArticel
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class WxSearchResultViewModel : MvvmBaseViewModel<WxSearchResultModel, WxArticel>() {
    fun init(key: String,cid:Int): WxSearchResultViewModel {
        model = WxSearchResultModel(key,cid)
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }

    fun refresh(key: String) {
        model.refresh(key)
    }

    fun loadNext(key: String) {
        model.loadNext(key)
    }
}