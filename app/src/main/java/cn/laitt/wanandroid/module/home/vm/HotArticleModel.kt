package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.TopArticle
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class HotArticleModel : MvvmBaseModel<TopArticle, ArrayList<Article.DataBean.DatasBean>>(
    TopArticle::class.java, false, PREF_KEY, null
) {
    companion object {
        private const val PREF_KEY = "pref_key_hot"
    }

    override fun onSuccess(t: TopArticle, isFromCache: Boolean) {
        var dataBeans: ArrayList<Article.DataBean.DatasBean> = java.util.ArrayList()
        dataBeans.addAll(t.data!!)
        //置顶
        dataBeans.map { it.top = true }
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
            .getTopArticle()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}