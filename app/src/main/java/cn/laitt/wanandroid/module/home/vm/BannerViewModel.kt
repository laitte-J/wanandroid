package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.Banner
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class BannerViewModel : MvvmBaseViewModel<BannerModel, Banner.DataBean>() {
    fun init(): BannerViewModel {
        model = BannerModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}