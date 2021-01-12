package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.Hotkey
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class SearchViewModel : MvvmBaseViewModel<SearchModel, Hotkey>() {
    fun init(): SearchViewModel {
        model = SearchModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}