package cn.laitt.wanandroid.module.knowledge.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.ActivityTreeBinding
import cn.laitt.wanandroid.model.Tree
import cn.laitt.wanandroid.module.knowledge.adapter.FragmentPager2Adapter
import cn.laitt.wanandroid.module.knowledge.ui.fragment.TreeListFragment
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.beans.BaseResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar

class TreeActivity : MvvmActivity<ActivityTreeBinding, MvvmBaseViewModel<*, *>?, BaseResponse>() {
    companion object {
        fun start(context: Context?, data: Tree.Data, index: Int) {
            val intent = Intent(context, TreeActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("index", index)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context!!.startActivity(intent)
        }
    }

    var data: Tree.Data? = null
    var fragmentList = ArrayList<Fragment>()
    var fragmentPager2Adapter: FragmentPager2Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .init();
        data = intent.getSerializableExtra("data") as Tree.Data?
        if (data != null) {
            data?.children?.forEach {
                fragmentList.add(TreeListFragment.create(it.id))
            }
        }
        viewDataBinding.tvTitle.apply {
            text = data!!.name
        }
        viewDataBinding.ivBack.apply {
            setOnClickListener { finish() }
        }
        fragmentPager2Adapter =
            FragmentPager2Adapter(supportFragmentManager, lifecycle, fragmentList)
        viewDataBinding.viewpager2.adapter = fragmentPager2Adapter
        TabLayoutMediator(
            viewDataBinding.tb, viewDataBinding.viewpager2, true, true
        ) { tab, position ->
            tab.text = data?.children?.get(position)?.name!!.toLowerCase()

        }.attach()
        viewDataBinding.tb.getTabAt(intent.getIntExtra("index", 0))!!.select()
    }

    override fun getViewModel(): MvvmBaseViewModel<*, *>? {
        return null
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun goToLogin() {
        LoginActivity.start(this)
    }

    override fun onListItemInserted(sender: ObservableList<BaseResponse>?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tree
    }
}