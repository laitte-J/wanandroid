package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.model.Wx
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class WxModel : MvvmBaseModel<Wx, ArrayList<Wx.Data>>(
    Wx::class.java, false, PREF_KEY, null
) {
    companion object {
        private const val PREF_KEY = "pref_key_wx"
    }

    override fun onSuccess(t: Wx, isFromCache: Boolean) {
        var dataBeans: ArrayList<Wx.Data> = java.util.ArrayList()
        dataBeans.addAll(t.data)
        loadSuccess(t, dataBeans, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    override fun refresh() {
        isRefresh = true
        load()
    }

    override fun load() {
        WanAndroidApi.getService(Knowledge::class.java)
            .getWx()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}