package cn.laitt.wanandroid.module.home.vm

import cn.laitt.wanandroid.model.StringResponse
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.beans.BaseResponse

 class CollectViewModel:MvvmBaseViewModel<CollectModel, BaseResponse>(){
    fun init() :CollectViewModel{
        model=CollectModel()
        model.register(this)
        return this
    }
    fun collect(cid: Int){
        model.collect(cid)
    }
}