package cn.laitt.wanandroid.module.login.vm

import cn.laitt.wanandroid.db.model.UserInfo
import cn.laitt.wanandroid.model.LoginBean
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class LoginViewModel : MvvmBaseViewModel<LoginModel, UserInfo>() {
    fun init(): LoginViewModel {
        model = LoginModel()
        model.register(this)
        return this
    }

    fun login(uname: String, pwd: String) {
        model.login(uname, pwd)
    }
}