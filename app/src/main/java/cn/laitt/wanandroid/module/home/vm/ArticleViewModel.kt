package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.TopArticle
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class ArticleViewModel : MvvmBaseViewModel<ArticleModel, Article.DataBean.DatasBean>() {
    fun init(): ArticleViewModel {
        model = ArticleModel();
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
    fun refresh() {
        model.refresh()
    }
    fun loadNext() {
        model.loadNext()
    }
}