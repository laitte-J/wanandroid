package cn.laitt.wanandroid.module.project.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.ActivityProjectBinding
import cn.laitt.wanandroid.model.Project
import cn.laitt.wanandroid.module.knowledge.adapter.FragmentPager2Adapter
import cn.laitt.wanandroid.module.knowledge.ui.fragment.TreeListFragment
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import cn.laitt.wanandroid.module.project.ui.fragment.ProjectListFragment
import cn.laitt.wanandroid.module.project.vm.ProjectViewModel
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar

class ProjectActivty : MvvmActivity<ActivityProjectBinding, ProjectViewModel, Project.Data>() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProjectActivty::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    var fragmentList = ArrayList<Fragment>()
    var fragmentPager2Adapter: FragmentPager2Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .init()
        viewDataBinding.ivBack.apply {
            setOnClickListener { finish() }
        }

        viewModel?.errorMessage?.observe(this, { s ->
            if (!TextUtils.isEmpty(s)) {
                ToastUtil.show(s)
            }
        })
    }

    override fun getViewModel(): ProjectViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable() = 0

    override fun goToLogin() {
        LoginActivity.start(this)
    }

    override fun onListItemInserted(sender: ObservableList<Project.Data>?) {
        if (sender != null) {
            fragmentList.clear()
            sender?.forEach {
                fragmentList.add(ProjectListFragment.create(it.id))
            }
            fragmentPager2Adapter =
                FragmentPager2Adapter(supportFragmentManager, lifecycle, fragmentList)
            viewDataBinding.viewpager2.adapter = fragmentPager2Adapter
            TabLayoutMediator(
                viewDataBinding.tb, viewDataBinding.viewpager2, true, true
            ) { tab, position ->
                tab.text = sender?.get(position)?.name!!.toLowerCase()

            }.attach()
        }

    }

    override fun getLayoutId() = R.layout.activity_project
}