package cn.laitt.wanandroid.module.knowledge.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.FragmentWxBinding
import cn.laitt.wanandroid.databinding.ListitemWxBinding
import cn.laitt.wanandroid.model.Wx
import cn.laitt.wanandroid.module.knowledge.ui.activity.WxArticelActivity
import cn.laitt.wanandroid.module.knowledge.vm.WxViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.fragment.MvvmFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import kotlinx.android.synthetic.main.fragment_wx.*

class WxFragment : MvvmFragment<FragmentWxBinding, WxViewModel, Wx.Data>() {
    companion object {
        fun create() = WxFragment()
    }

    lateinit var adapter: BaseQuickAdapter<Wx.Data, BaseDataBindingHolder<ListitemWxBinding>>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcv.layoutManager = LinearLayoutManager(context)
        adapter = object :
            BaseQuickAdapter<Wx.Data, BaseDataBindingHolder<ListitemWxBinding>>(R.layout.listitem_wx) {
            override fun convert(holder: BaseDataBindingHolder<ListitemWxBinding>, item: Wx.Data) {
                holder.dataBinding?.model = item
            }
        }
        rcv.adapter = adapter
        adapter.setEmptyView(R.layout.layout_state_loading)
        adapter.apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    ad: BaseQuickAdapter<*, *>,
                    view: View,
                    position: Int
                ) {
                    WxArticelActivity.start(context!!, "", adapter.data[position].id)
                }
            })
        }
    }

    override fun getBindingVariable() = 0

    override fun getLayoutId() = R.layout.fragment_wx

    override fun getViewModel(): WxViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(WxViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun onListItemInserted(sender: ObservableList<Wx.Data>?) {
        adapter.setList(sender)
        adapter.setEmptyView(R.layout.layout_state_empty)

    }

    override fun getFragmentTag() = javaClass.canonicalName

    override fun goToLogin() {
        LoginActivity.start(requireContext())
    }

}