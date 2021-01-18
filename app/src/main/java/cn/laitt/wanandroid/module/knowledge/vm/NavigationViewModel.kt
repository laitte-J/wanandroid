package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Navigation
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NavigationViewModel : MvvmBaseViewModel<NavigationModel, Navigation>(),
    CoroutineScope by MainScope() {
    fun init(): NavigationViewModel {
        model = NavigationModel()
        model.register(this)
        model.getCachedDataAndLoad()
//        launch { model.getCachedDataAndLoad() }
        return this
    }

//    fun refresh() {
//        model.refresh()
//    }
}