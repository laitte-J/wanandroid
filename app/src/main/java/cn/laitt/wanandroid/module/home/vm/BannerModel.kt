package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.model.Banner
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class BannerModel : MvvmBaseModel<Banner, ArrayList<Banner.DataBean>>(
    Banner::class.java, false, PREF_KEY, null
) {
    companion object {
        private const val PREF_KEY = "pref_key_banner"
    }

    override fun onSuccess(t: Banner, isFromCache: Boolean) {
        var dataBeans: ArrayList<Banner.DataBean> = java.util.ArrayList()
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
            .getBanner()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}