package cn.laitt.wanandroid.module.knowledge.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.FragmentTreeBinding
import cn.laitt.wanandroid.databinding.ListitemHotkeyBinding
import cn.laitt.wanandroid.databinding.ListitemTreeL1Binding
import cn.laitt.wanandroid.model.Tree
import cn.laitt.wanandroid.module.knowledge.ui.activity.TreeActivity
import cn.laitt.wanandroid.module.knowledge.vm.KnowledgeViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.fragment.MvvmFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.android.flexbox.FlexboxLayoutManager

class TreeFragment : MvvmFragment<FragmentTreeBinding, KnowledgeViewModel, Tree.Data>() {
    var adapter: BaseQuickAdapter<Tree.Data, BaseDataBindingHolder<ListitemTreeL1Binding>>? = null

    companion object {
        fun create(): TreeFragment {
            return TreeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.rcv.layoutManager = LinearLayoutManager(context)
        adapter = object :
            BaseQuickAdapter<Tree.Data, BaseDataBindingHolder<ListitemTreeL1Binding>>(R.layout.listitem_tree_l1) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemTreeL1Binding>,
                item: Tree.Data
            ) {
                var databing = holder.dataBinding
                if (databing != null) {
                    databing.model = item
                    databing.rcv.setNestedScrollingEnabled(false)
                    databing.rcv.setHasFixedSize(true)
                    databing.rcv.layoutManager = FlexboxLayoutManager(getContext())
                    var adapter = object :
                        BaseQuickAdapter<Tree.Children, BaseDataBindingHolder<ListitemHotkeyBinding>>(
                            R.layout.listitem_hotkey
                        ) {
                        override fun convert(
                            holder: BaseDataBindingHolder<ListitemHotkeyBinding>,
                            item1: Tree.Children
                        ) {
                            var databing = holder.dataBinding
                            if (databing != null) {
                                databing.root.setOnClickListener { TreeActivity.start(getContext(),item,holder.adapterPosition) }
                                databing.tvName.text = item1.name
                            }
                        }
                    }
                    databing.rcv.adapter = adapter
                    adapter.setList(item.children)
                }
            }

        }

        viewDataBinding.rcv.adapter = adapter
        viewModel?.errorMessage?.observe(viewLifecycleOwner, { s ->
            if (!TextUtils.isEmpty(s)) {
                adapter?.setEmptyView(R.layout.layout_state_error)

            }
        })
        adapter?.setEmptyView(R.layout.layout_state_loading)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree
    }

    override fun getViewModel(): KnowledgeViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(KnowledgeViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun onListItemInserted(sender: ObservableList<Tree.Data>) {
        adapter?.setList(sender)
        adapter?.setEmptyView(R.layout.layout_state_empty)
    }

    override fun getFragmentTag(): String {
        return javaClass.canonicalName
    }

    override fun goToLogin() {  LoginActivity.start(requireContext())}
}