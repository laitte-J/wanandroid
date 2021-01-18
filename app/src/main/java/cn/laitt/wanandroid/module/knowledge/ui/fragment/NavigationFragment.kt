package cn.laitt.wanandroid.module.knowledge.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.FragmentNavigationBinding
import cn.laitt.wanandroid.databinding.ListitemHotkeyBinding
import cn.laitt.wanandroid.databinding.ListitemNviBinding
import cn.laitt.wanandroid.databinding.ListitemNviMainBinding
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.Data
import cn.laitt.wanandroid.module.knowledge.vm.NavigationViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.fragment.MvvmFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.emcrp.webview.WebviewActivity
import com.google.android.flexbox.FlexboxLayoutManager

class NavigationFragment : MvvmFragment<FragmentNavigationBinding, NavigationViewModel, Data>() {
    companion object {
        fun create() = NavigationFragment()
    }

    //目标项是否在最后一个可见项之后
    private var mShouldScroll = false

    //记录目标项位置
    private var mToPosition = 0
    private val sIsScrolling = false
    private var index = 0


    override fun getBindingVariable() = 0
    override fun getLayoutId() = R.layout.fragment_navigation
    var mAdapterv1: BaseQuickAdapter<Data, BaseDataBindingHolder<ListitemNviMainBinding>>? = null
    var mAdapterv2: BaseQuickAdapter<Data, BaseDataBindingHolder<ListitemNviBinding>>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.recyclerLevel1.layoutManager = LinearLayoutManager(context)
        viewDataBinding.recyclerLevel2.layoutManager = LinearLayoutManager(context)
        viewDataBinding.progress.show()
        viewModel.errorMessage.observe(viewLifecycleOwner, { s ->
            if (!TextUtils.isEmpty(s)) {
                viewDataBinding.progress.hide()
            }
        })
        mAdapterv1 = object :
            BaseQuickAdapter<Data, BaseDataBindingHolder<ListitemNviMainBinding>>(R.layout.listitem_nvi_main) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemNviMainBinding>,
                item: Data
            ) {
                holder.dataBinding!!.model = item
            }

        }
        viewDataBinding.recyclerLevel1.adapter = mAdapterv1

        mAdapterv1?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (index != position) {
                    mAdapterv1?.data!![index].select = false
                    mAdapterv1?.notifyItemChanged(index)
                    mAdapterv1?.data!![position].select = true
                    mAdapterv1?.notifyItemChanged(position)
                    index = position
                    if (position != -1) {
                        smoothMoveToPosition(viewDataBinding.recyclerLevel2, position)
                    } else {
                        smoothMoveToPosition(viewDataBinding.recyclerLevel2, position + 1)
                    }
                }

            }
        })

        mAdapterv2 = object :
            BaseQuickAdapter<Data, BaseDataBindingHolder<ListitemNviBinding>>(R.layout.listitem_nvi) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemNviBinding>,
                item: Data
            ) {
                holder.dataBinding!!.model = item
                holder.dataBinding!!.recyclerView.layoutManager = FlexboxLayoutManager(context)
                val mAdapter = object :
                    BaseQuickAdapter<Article.DataBean.DatasBean, BaseDataBindingHolder<ListitemHotkeyBinding>>(
                        R.layout.listitem_hotkey
                    ) {
                    override fun convert(
                        holder: BaseDataBindingHolder<ListitemHotkeyBinding>,
                        item: Article.DataBean.DatasBean
                    ) {
                        holder.dataBinding!!.tvName.text = item.title
                    }
                }
                holder.dataBinding!!.recyclerView.adapter = mAdapter
                mAdapter.setList(item.articles)
                mAdapter.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(
                        adapter: BaseQuickAdapter<*, *>,
                        view: View,
                        position: Int
                    ) {
                        if (!TextUtils.isEmpty(item.articles[position].link))
                            WebviewActivity.startCommonWeb(
                                context,
                                item.articles[position].title,
                                item.articles[position].link
                            )
                    }
                })
            }

        }
        viewDataBinding.recyclerLevel2.adapter = mAdapterv2
        viewDataBinding.recyclerLevel2.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mShouldScroll) {
                    mShouldScroll = false;
                    smoothMoveToPosition(viewDataBinding.recyclerLevel2, mToPosition);
                }
            }
        })
//        viewModel.refresh()
    }

    /**
     * 滑动到指定位置
     */
    private fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) {
        // 第一个可见位置
        val firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0))
        // 最后一个可见位置
        val lastItem =
            mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.childCount - 1))
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top = mRecyclerView.getChildAt(movePosition).top
                mRecyclerView.smoothScrollBy(0, top)
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }

    override fun getViewModel(): NavigationViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(NavigationViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun onListItemInserted(sender: ObservableList<Data>) {
        viewDataBinding.progress.hide()
        mAdapterv1!!.setList(sender)
        mAdapterv2!!.setList(sender)
    }

    override fun getFragmentTag() = javaClass.canonicalName

    override fun goToLogin() {
        LoginActivity.start(requireContext())
    }
}