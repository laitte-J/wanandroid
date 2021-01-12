package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.model.Wx
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class WxViewModel : MvvmBaseViewModel<WxModel, Wx.Data>() {
    fun init(): WxViewModel {
        model = WxModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}