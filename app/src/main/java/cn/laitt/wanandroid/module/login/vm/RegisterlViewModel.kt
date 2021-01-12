package cn.laitt.wanandroid.module.login.vm

import cn.laitt.wanandroid.db.model.UserInfo
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class RegisterlViewModel : MvvmBaseViewModel<RegisterModel, UserInfo>() {
    fun init(): RegisterlViewModel {
        model = RegisterModel()
        model.register(this)
        return this
    }

    fun register(uname: String, pwd: String, rePwd: String) {
        model.register(uname, pwd, rePwd)
    }
}