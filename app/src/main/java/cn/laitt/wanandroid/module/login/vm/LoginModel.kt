package cn.laitt.wanandroid.module.login.vm

import cn.laitt.wanandroid.api.Login
import cn.laitt.wanandroid.db.model.UserInfo
import cn.laitt.wanandroid.model.LoginBean
import com.arch.base.core.model.MvvmBaseModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.observer.BaseObserver

class LoginModel : MvvmBaseModel<LoginBean, ArrayList<UserInfo>>(
    LoginBean::class.java,
    false,
    null,
    null
) {
    var uname: String? = null
    var pwd: String? = null
    override fun onSuccess(t: LoginBean?, isFromCache: Boolean) {
        var datas = ArrayList<UserInfo>()
        datas.add(t!!.data!!)
        loadSuccess(t, datas, isFromCache)
    }

    fun login(uname: String, pwd: String) {
        this.uname = uname
        this.pwd = pwd
        load()
    }

    override fun onFailure(e: Throwable?) {
        loadFail(e)
    }

    override fun refresh() {
        load()
    }

    override fun load() {
        WanAndroidApi.getService(Login::class.java)
            .login(uname, pwd)
            .compose(WanAndroidApi.getInstance().applySchedulers(BaseObserver(this, this)))
    }
}