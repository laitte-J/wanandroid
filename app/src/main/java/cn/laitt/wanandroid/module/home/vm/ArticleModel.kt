package cn.laitt.wanandroid.module.home.vm

import android.util.Log
import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.model.Article
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class ArticleModel : MvvmBaseModel<Article, ArrayList<Article.DataBean.DatasBean>>(
    Article::class.java, true, PREF_KEY, null,1
) {
    companion object {
        private const val PREF_KEY = "pref_key_article"
    }

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
        WanAndroidApi.getService(Home::class.java)
            .getArticle(pageNumber)
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}