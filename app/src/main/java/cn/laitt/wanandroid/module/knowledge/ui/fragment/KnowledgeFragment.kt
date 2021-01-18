@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package cn.laitt.wanandroid.module.mine.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.FragmentKnowledgeBinding
import cn.laitt.wanandroid.module.knowledge.adapter.FragmentPager2Adapter
import cn.laitt.wanandroid.module.knowledge.ui.fragment.NavigationFragment
import cn.laitt.wanandroid.module.knowledge.ui.fragment.TreeFragment
import cn.laitt.wanandroid.module.knowledge.ui.fragment.WxFragment
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.fragment.MvvmFragment
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.beans.BaseResponse
import com.google.android.material.tabs.TabLayoutMediator


class KnowledgeFragment :
    MvvmFragment<FragmentKnowledgeBinding, MvvmBaseViewModel<*, *>, BaseResponse?>() {


    var fragmentList = ArrayList<Fragment>()
    var fragmentPager2Adapter: FragmentPager2Adapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentList.add(NavigationFragment.create())
        fragmentList.add(TreeFragment.create())
        fragmentList.add(WxFragment.create())

        fragmentPager2Adapter = FragmentPager2Adapter(childFragmentManager, lifecycle, fragmentList)
        viewDataBinding.viewpager2.adapter = fragmentPager2Adapter
        TabLayoutMediator(
            viewDataBinding.tb, viewDataBinding.viewpager2, true, true
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "导航"
                1 -> tab.text = "体系"
                2 -> tab.text = "公众号"
            }
        }.attach()

    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_knowledge
    }

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