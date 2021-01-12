package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.api.Knowledge
import cn.laitt.wanandroid.model.StringResponse
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.beans.BaseResponse
import com.emcrp.network.observer.BaseObserver

class CollectModel : MvvmBaseModel<BaseResponse, ArrayList<BaseResponse>>(
    BaseResponse::class.java,
    false,
    null,
    null
) {
    private var cid: Int = -1
    override fun onSuccess(t: BaseResponse, isFromCache: Boolean) {
        var datas = ArrayList<BaseResponse>()
        datas.add(t)
        loadSuccess(t, datas, isFromCache)
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    fun collect(cid: Int) {
        this.cid = cid
        load()
    }

    override fun refresh() {
        load()
    }

    override fun load() {
        if (cid != -1)
            WanAndroidApi.getService(Knowledge::class.java)
                .collect(cid)
                .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}