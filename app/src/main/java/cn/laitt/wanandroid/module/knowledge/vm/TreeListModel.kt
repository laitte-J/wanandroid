package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Article
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class TreeListModel(var cid: Int) : MvvmBaseModel<Article, ArrayList<Article.DataBean.DatasBean>>(
    Article::class.java, true, null, null, 1
) {


    override fun onSuccess(t: Article, isFromCache: Boolean) {
        var dataBeans: ArrayList<Article.DataBean.DatasBean> = java.util.ArrayList()
        dataBeans.addAll(t.data!!.datas!!)
        loadSuccess(t, dataBeans, isFromCache)
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
            .getArticle(pageNumber, cid)
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}