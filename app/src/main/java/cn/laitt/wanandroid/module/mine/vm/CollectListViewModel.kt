package cn.laitt.wanandroid.module.mine.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class CollectListViewModel : MvvmBaseViewModel<CollectListModel, Article.DataBean.DatasBean>() {
    fun init(): CollectListViewModel {
        model = CollectListModel()
        model.register(this)
//        model.getCachedDataAndLoad()
        return this
    }

    fun loadNext() {
        model.loadNext()
    }
}