package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.model.Hotkey
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class SearchModel : MvvmBaseModel<Hotkey, ArrayList<Hotkey.DataBean>>(
    Hotkey::class.java, false, PREF_KEY, null
) {
    companion object {
        private const val PREF_KEY = "pref_key_hotkey"
    }

    override fun onSuccess(t: Hotkey, isFromCache: Boolean) {
        var dataBeans: ArrayList<Hotkey.DataBean> = java.util.ArrayList()
        dataBeans.addAll(t.data!!)
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
        WanAndroidApi.getService(Home::class.java)
            .getHotKey()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}