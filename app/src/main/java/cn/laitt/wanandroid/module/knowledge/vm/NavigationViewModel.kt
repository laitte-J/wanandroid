package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Navigation
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class NavigationViewModel : MvvmBaseViewModel<NavigationModel, Navigation>() {
    fun init(): NavigationViewModel {
        model = NavigationModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}