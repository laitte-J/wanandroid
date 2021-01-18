package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Data
import cn.laitt.wanandroid.model.Navigation
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class NavigationModel :
    MvvmBaseModel<Navigation, ArrayList<Data>>(Navigation::class.java, false, KEY, null) {
    companion object {
        val KEY = "Navigation"
    }

    override fun onSuccess(t: Navigation?, isFromCache: Boolean) {
        var data = ArrayList<Data>()
        data.addAll(t!!.data)
        data[0].select = true
        loadSuccess(t, data, isFromCache)
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
            .getNvi()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}