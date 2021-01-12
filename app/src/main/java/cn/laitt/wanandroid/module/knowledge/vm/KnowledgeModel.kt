package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.api.Home
import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.model.Tree
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class KnowledgeModel : MvvmBaseModel<Tree, ArrayList<Tree.Data>>(
    Tree::class.java, false, PREF_KEY, null
) {
    companion object {
        private const val PREF_KEY = "pref_key_tree"
    }

    override fun onSuccess(t: Tree, isFromCache: Boolean) {
        var dataBeans: ArrayList<Tree.Data> = java.util.ArrayList()
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
        WanAndroidApi.getService(Knowledge::class.java)
            .getTree()
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}