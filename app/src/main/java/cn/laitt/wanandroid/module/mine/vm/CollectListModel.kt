package cn.laitt.wanandroid.module.mine.vm

import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Article
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class CollectListModel() : MvvmBaseModel<Article, ArrayList<Article.DataBean.DatasBean>>(
    cn.laitt.wanandroid.model.Article::class.java, true,
    "CollectListKEY", null, 1
) {
    override fun onSuccess(t: Article, isFromCache: Boolean) {
        var datas = ArrayList<Article.DataBean.DatasBean>()
        datas.addAll(t.data!!.datas!!)
        datas.forEach {
            it.collect = true
            it.top = false
            it.fresh = false
        }
        loadSuccess(t, datas, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    override fun refresh() {
        isRefresh = true
        load()
    }


    fun loadNext() {
        isRefresh = false
        load()
    }

    override fun load() {
        pageNumber = if (isRefresh) 0 else pageNumber
        WanAndroidApi.getService(Knowledge::class.java)
            .collectList(pageNumber)
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}