package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class TreeListViewModel : MvvmBaseViewModel<TreeListModel, Article.DataBean.DatasBean>() {
    fun init(cid: Int): TreeListViewModel {
        model = TreeListModel(cid)
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }


    fun loadNext() {
        model.loadNext()
    }
}