package cn.laitt.wanandroid.module.project.vm

import cn.laitt.wanandroid.model.Project
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class ProjectModel : MvvmBaseModel<Project, ArrayList<Project.Data>>(
    Project::class.java,
    false,
    "Project_Tree",
    null
) {
    override fun onSuccess(t: Project, isFromCache: Boolean) {
        loadSuccess(t, ArrayList(t.data), isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    override fun refresh() {
        load()
    }

    override fun load() {
        WanAndroidApi.getService(cn.laitt.wanandroid.api.Project::class.java)
            .getProjectTree()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}