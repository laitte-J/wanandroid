package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.api.Knowledge
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.beans.BaseResponse
import com.emcrp.network.observer.BaseObserver

class UnCollectModel : MvvmBaseModel<BaseResponse, ArrayList<BaseResponse>>(
    BaseResponse::class.java,
    false,
    null,
    null
) {
    private var originId: Int = -1
    private var cid: Int = -1
    override fun onSuccess(t: BaseResponse, isFromCache: Boolean) {
        var datas = ArrayList<BaseResponse>()
        datas.add(t)
        loadSuccess(t, datas, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    fun uncollectArticle(cid: Int) {
        this.cid = cid
        load()
    }

    override fun refresh() {
        load()
    }

    fun uncollectArticle(cid: Int, originId: Int) {
        this.cid = cid
        this.originId = originId
        loadforList()
    }

    override fun load() {
        if (cid != -1)
            WanAndroidApi.getService(Knowledge::class.java)
                .uncollectArticle(cid)
                .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }

  private  fun loadforList() {
        if (cid != -1 && originId != -1)
            WanAndroidApi.getService(Knowledge::class.java)
                .uncollectArticle(cid, originId)
                .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}