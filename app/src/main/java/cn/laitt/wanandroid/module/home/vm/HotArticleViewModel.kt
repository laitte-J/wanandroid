package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class HotArticleViewModel : MvvmBaseViewModel<HotArticleModel, Article.DataBean.DatasBean>() {
    fun init(): HotArticleViewModel {
        model = HotArticleModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }

    fun load() {
        model.refresh()
    }
}