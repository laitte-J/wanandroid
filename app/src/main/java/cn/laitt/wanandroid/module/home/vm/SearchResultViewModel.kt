package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class SearchResultViewModel : MvvmBaseViewModel<SearchResultModel, Article.DataBean.DatasBean>() {
    fun init(key: String): SearchResultViewModel {
        model = SearchResultModel(key)
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