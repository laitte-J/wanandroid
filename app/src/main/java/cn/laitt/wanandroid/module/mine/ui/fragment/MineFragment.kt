package cn.laitt.wanandroid.module.mine.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.api.Login
import cn.laitt.wanandroid.commom.Contact
import cn.laitt.wanandroid.databinding.FragmentMineBinding
import cn.laitt.wanandroid.db.model.UserInfo
import cn.laitt.wanandroid.db.vm.UserInfoViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import cn.laitt.wanandroid.module.mine.ui.activity.CollectListActivity
import com.arch.base.common.utils.LiveDataBus
import com.arch.base.core.fragment.MvvmFragment
import com.arch.base.core.utils.ToastUtil
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.beans.BaseResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MineFragment : MvvmFragment<FragmentMineBinding, MvvmBaseViewModel<*, *>, BaseResponse?>(),
    CoroutineScope by MainScope() {
    private var userInfoViewModel: UserInfoViewModel? = null
    var user: UserInfo? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.tvOut.apply {
            setOnClickListener {
                if (user == null) {
                    goToLogin()
                } else
                    WanAndroidApi.getService(Login::class.java)
                        .logout()
                        .subscribeOn(Schedulers.io())
                        .throttleFirst(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseResponse> {
                            override fun onSubscribe(d: Disposable?) {
                            }

                            override fun onNext(t: BaseResponse?) {
                                launch {
                                    withContext(Dispatchers.IO) {
                                        //登出成功清除用户信息
                                        userInfoViewModel?.deleteAll()
                                    }
                                    LiveDataBus.get().with(Contact.LOGINOUT_KEY)
                                        .setValue(Contact.LOGINOUT_VALUE)
                                    ToastUtil.show("登出成功！")
                                }
                            }

                            override fun onError(e: Throwable?) {
                                ToastUtil.show(e?.localizedMessage)
                            }

                            override fun onComplete() {
                            }

                        })

            }
        }
        userInfoViewModel =
            ViewModelProvider(this).get(UserInfoViewModel::class.java)
        userInfoViewModel?.getliveDataSearchUser()
            ?.observe(viewLifecycleOwner, { datas ->
                if (datas.size == 0) {
                    Log.w(tag, "未登录/用户信息已清空")
                    user = null
                    viewDataBinding.tvName.text = "请登录"
                    viewDataBinding.tvId.text = getString(R.string.uid, "xxxxx")
                } else {
                    Log.w(tag, "用户信息发生变化")
                    user = datas[0]
                    viewDataBinding.user = user
                    viewDataBinding.executePendingBindings()
                }
            })

        LiveDataBus.get()
            .with(Contact.RELOGIN_USER, UserInfo::class.java)
            .observe(viewLifecycleOwner, {
                Log.w(tag, "onActivityCreated:重新登录 刷新数据! 用户====== " + it.toString())
                user = it
                viewDataBinding.user = user
                viewDataBinding.executePendingBindings()
            })
        viewDataBinding.llCollect.setOnClickListener { CollectListActivity.start(requireContext()) }
    }


    override fun getBindingVariable() = 0


    override fun getLayoutId() = R.layout.fragment_mine


    override fun getViewModel(): MvvmBaseViewModel<*, *>? {
        return null
    }

    override fun onListItemInserted(sender: ObservableList<BaseResponse?>?) {

    }

    override fun getFragmentTag(): String {
        return javaClass.canonicalName
    }

    override fun goToLogin() {
        LoginActivity.start(requireContext())
    }
}