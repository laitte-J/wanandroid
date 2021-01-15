package cn.laitt.wanandroid.module.project.vm

import cn.laitt.wanandroid.model.Project
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class ProjectViewModel : MvvmBaseViewModel<ProjectModel, Project.Data>() {
    fun init(): ProjectViewModel {
        model = ProjectModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}