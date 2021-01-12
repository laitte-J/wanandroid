package cn.laitt.wanandroid.module.knowledge.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class FragmentPager2Adapter : FragmentStateAdapter {
    private var fragmentList = ArrayList<Fragment>()

    constructor(fragmentActivity: FragmentActivity, fragmentList: ArrayList<Fragment>) : super(
        fragmentActivity
    ) {
        this.fragmentList = fragmentList
    }

    constructor(fragment: Fragment, fragmentList: ArrayList<Fragment>) : super(fragment) {
        this.fragmentList = fragmentList
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        fragmentList: ArrayList<Fragment>
    ) : super(fragmentManager, lifecycle) {
        this.fragmentList = fragmentList
    }


    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}