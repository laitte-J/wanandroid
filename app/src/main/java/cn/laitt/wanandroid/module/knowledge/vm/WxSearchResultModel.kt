package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.DataX
import cn.laitt.wanandroid.model.WxArticel
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class WxSearchResultModel(var key: String?=null,var cid:Int) : MvvmBaseModel<WxArticel, ArrayList<DataX>>(
    cn.laitt.wanandroid.model.WxArticel::class.java, true,
    null, null, 1
) {
    override fun onSuccess(t: WxArticel, isFromCache: Boolean) {
        var datas = ArrayList<DataX>()
        datas.addAll(t.data!!.datas!!)
        loadSuccess(t, datas, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    override fun refresh() {
        isRefresh = true
        load()
    }

    fun refresh(key: String?=null) {
        isRefresh = true
        this.key = key
        load()
    }

    fun loadNext(key: String?=null) {
        isRefresh = false
        this.key = key
        load()
    }

    override fun load() {
        pageNumber = if (isRefresh) 0 else pageNumber
        WanAndroidApi.getService(Knowledge::class.java)
            .getWxArticle(pageNumber,cid, key)
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}