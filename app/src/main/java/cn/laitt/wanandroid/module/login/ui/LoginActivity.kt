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
import kotlinx.android.synthetic.main.activity_search.*
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

            launch {
                withContext(Dispatchers.IO) {
                    userInfoViewModel?.insert(sender[0])
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_login
}