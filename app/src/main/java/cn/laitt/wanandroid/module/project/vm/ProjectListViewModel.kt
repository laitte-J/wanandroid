package cn.laitt.wanandroid.module.project.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class ProjectListViewModel : MvvmBaseViewModel<ProjectListModel, Article.DataBean.DatasBean>() {
    fun init(cid: Int): ProjectListViewModel {
        model = ProjectListModel(cid)
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }


    fun loadNext() {
        model.loadNext()
    }
}