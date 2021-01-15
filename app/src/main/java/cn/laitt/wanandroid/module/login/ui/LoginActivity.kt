package cn.laitt.wanandroid.module.login.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.commom.Contact
import cn.laitt.wanandroid.databinding.ActivityLoginBinding
import cn.laitt.wanandroid.db.model.UserInfo
import cn.laitt.wanandroid.db.vm.UserInfoViewModel
import cn.laitt.wanandroid.module.login.vm.LoginViewModel
import com.arch.base.common.utils.LiveDataBus
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.*


class LoginActivity : MvvmActivity<ActivityLoginBinding, LoginViewModel, UserInfo>(),
    CoroutineScope by MainScope() {


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var userInfoViewModel: UserInfoViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .statusBarAlpha(0f).transparentStatusBar().init()
        viewDataBinding.ivBack.apply { setOnClickListener { finish() } }
        viewDataBinding.tvRegister.setOnClickListener {
            RegisterActivity.start(this@LoginActivity)
            finish()
        }

        viewDataBinding.progress.hide()
        viewDataBinding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) {
                    viewDataBinding.ivClearUser.visibility = View.INVISIBLE
                    viewDataBinding.btnLogin.isEnabled = false
                } else {
                    if (TextUtils.isEmpty(viewDataBinding.etPassWord.text.toString().trim())) {
                        viewDataBinding.btnLogin.isEnabled = false
                    } else {
                        viewDataBinding.btnLogin.isEnabled = true
                    }
                    viewDataBinding.ivClearUser.visibility = View.VISIBLE
                }
            }
        })
        viewModel.errorMessage.observe(
            this,
            { s ->
                if (!TextUtils.isEmpty(s)) {
                    viewDataBinding.progress.hide()
                    viewDataBinding.btnLogin.isEnabled = true
                }
            })
        viewDataBinding.etPassWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) {
                    viewDataBinding.btnLogin.isEnabled = false
                    viewDataBinding.ivShowPwd.visibility = View.INVISIBLE
                } else {
                    if (TextUtils.isEmpty(viewDataBinding.etUserName.text.toString().trim())) {
                        viewDataBinding.btnLogin.isEnabled = false
                    } else {
                        viewDataBinding.btnLogin.isEnabled = true
                    }
                    viewDataBinding.ivShowPwd.visibility = View.VISIBLE
                }
            }
        })
        viewDataBinding.btnLogin.setOnClickListener { view ->
            if (TextUtils.isEmpty(
                    viewDataBinding.etUserName.text.toString().trim()
                ) ||
                TextUtils.isEmpty(
                    viewDataBinding.etPassWord.text.toString().trim()
                )
            ) {
                ToastUtil.show("请输入账号以及密码！")
                return@setOnClickListener
            }
            viewDataBinding.btnLogin.isEnabled = false
            viewDataBinding.progress.show()
            viewModel.login(
                viewDataBinding.etUserName.text.toString().trim(),
                viewDataBinding.etPassWord.text.toString().trim()
            )
        }
        viewDataBinding.ivShowPwd.setOnClickListener { view ->
            if (viewDataBinding.ivShowPwd.isActivated) {
                viewDataBinding.etPassWord.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            } else {
                viewDataBinding.etPassWord.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            viewDataBinding.ivShowPwd.isActivated = !viewDataBinding.ivShowPwd.isActivated
        }
        viewDataBinding.ivClearUser.setOnClickListener { view ->
            viewDataBinding.etUserName.setText("")
            viewDataBinding.ivClearUser.visibility = View.INVISIBLE
        }
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)

        userInfoViewModel?.getliveDataSearchUser()?.observe(this, { datas ->
            if (datas.size > 0) {
                LiveDataBus.get().with(Contact.RELOGIN_KEY).setValue(Contact.RELOGIN_VALUE)
                LiveDataBus.get().with(Contact.RELOGIN_USER).setValue(datas[0])
                datas.map { Log.w("用户信息", "用户信息已保存" + it.username) }
                finish()
            }

        })
    }

    override fun getViewModel(): LoginViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(LoginViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable() = 0

    override fun goToLogin() {
    }

    override fun onListItemInserted(sender: ObservableList<UserInfo>?) {
        viewDataBinding.progress.hide()
        if (sender != null && sender.size > 0) {
            viewDataBinding.progress.hide()
            viewDataBinding.btnLogin.isEnabled = true
            sender[0].icon =
                "data:image/jpeg;base64,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"
            launch {
                withContext(Dispatchers.IO) {
                    userInfoViewModel?.insert(sender[0])
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_login
}