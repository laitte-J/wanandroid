package cn.laitt.wanandroid.module.login.ui

import android.Manifest
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
import cn.laitt.wanandroid.databinding.ActivityRegisterBinding
import cn.laitt.wanandroid.db.model.UserInfo
import cn.laitt.wanandroid.db.vm.UserInfoViewModel
import cn.laitt.wanandroid.module.login.vm.RegisterlViewModel
import com.arch.base.common.utils.LiveDataBus
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.*

class RegisterActivity : MvvmActivity<ActivityRegisterBinding, RegisterlViewModel, UserInfo>(),
    CoroutineScope by MainScope() {
    private var userInfoViewModel: UserInfoViewModel? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .statusBarAlpha(0f).transparentStatusBar().init()
        viewDataBinding.ivBack.apply { setOnClickListener { finish() } }
        viewDataBinding.progress.hide()
        viewDataBinding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) {
                    viewDataBinding.ivClearUser.visibility = View.INVISIBLE
                    viewDataBinding.btnRegister.isEnabled = false
                } else {
                    if (TextUtils.isEmpty(viewDataBinding.etPassWord.text.toString().trim())) {
                        viewDataBinding.btnRegister.isEnabled = false
                    } else {
                        viewDataBinding.btnRegister.isEnabled = true
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
                    viewDataBinding.btnRegister.isEnabled = true
                }
            })
        viewDataBinding.etPassWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) {
                    viewDataBinding.btnRegister.isEnabled = false
                    viewDataBinding.ivShowPwd.visibility = View.INVISIBLE
                } else {
                    if (TextUtils.isEmpty(viewDataBinding.etUserName.text.toString().trim())) {
                        viewDataBinding.btnRegister.isEnabled = false
                    } else {
                        viewDataBinding.btnRegister.isEnabled = true
                    }
                    viewDataBinding.ivShowPwd.visibility = View.VISIBLE
                }
            }
        })
        viewDataBinding.etPassWordEnter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (TextUtils.isEmpty(editable.toString().trim { it <= ' ' })) {
                    viewDataBinding.btnRegister.isEnabled = false
                    viewDataBinding.ivShowPwdEnter.visibility = View.INVISIBLE
                } else {
                    if (TextUtils.isEmpty(viewDataBinding.etUserName.text.toString().trim())) {
                        viewDataBinding.btnRegister.isEnabled = false
                    } else {
                        viewDataBinding.btnRegister.isEnabled = true
                    }
                    viewDataBinding.ivShowPwdEnter.visibility = View.VISIBLE
                }
            }
        })
        viewDataBinding.btnRegister.setOnClickListener { view ->
            if (TextUtils.isEmpty(
                    viewDataBinding.etUserName.text.toString().trim()
                ) ||
                TextUtils.isEmpty(
                    viewDataBinding.etPassWord.text.toString().trim()
                )
                ||
                TextUtils.isEmpty(
                    viewDataBinding.etPassWordEnter.text.toString().trim()
                )
            ) {
                ToastUtil.show("请输入账号以及密码！")
                return@setOnClickListener
            }
            viewDataBinding.btnRegister.isEnabled = false
            viewDataBinding.progress.show()
            viewModel.register(
                viewDataBinding.etUserName.text.toString().trim(),
                viewDataBinding.etPassWord.text.toString().trim(),
                viewDataBinding.etPassWordEnter.text.toString().trim()
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
        viewDataBinding.ivShowPwdEnter.setOnClickListener { view ->
            if (viewDataBinding.ivShowPwdEnter.isActivated) {
                viewDataBinding.etPassWordEnter.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            } else {
                viewDataBinding.etPassWordEnter.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            }
            viewDataBinding.ivShowPwdEnter.isActivated = !viewDataBinding.ivShowPwdEnter.isActivated
        }

        viewDataBinding.ivClearUser.setOnClickListener { view ->
            viewDataBinding.etUserName.setText("")
            viewDataBinding.ivClearUser.visibility = View.INVISIBLE
        }
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)

        userInfoViewModel?.getliveDataSearchUser()?.observe(this, { datas ->
            if (datas.size == 1) {
                LiveDataBus.get().with(Contact.RELOGIN_KEY).setValue(Contact.RELOGIN_VALUE)
                LiveDataBus.get().with(Contact.RELOGIN_USER).setValue(datas[0].id)
                datas.map { Log.w("用户信息", "用户信息已保存" + it.username) }
                finish()
            }

        })
    }

    override fun getViewModel(): RegisterlViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(RegisterlViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable() = 0

    override fun goToLogin() {
//        LoginActivity.start(this)
//        finish()
    }

    override fun onListItemInserted(sender: ObservableList<UserInfo>) {
        viewDataBinding.progress.hide()
        if (sender != null && sender.size > 0) {
            viewDataBinding.progress.hide()
            viewDataBinding.btnRegister.isEnabled = true
            launch {
                withContext(Dispatchers.IO) {
                    userInfoViewModel?.insert(sender[0])
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_register
}