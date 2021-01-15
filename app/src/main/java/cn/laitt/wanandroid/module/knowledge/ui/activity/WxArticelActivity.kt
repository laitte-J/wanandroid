package cn.laitt.wanandroid.module.knowledge.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.ActivityWxarticleBinding
import cn.laitt.wanandroid.databinding.ListitemWxArticleBinding
import cn.laitt.wanandroid.model.DataX
import cn.laitt.wanandroid.module.knowledge.vm.WxSearchResultViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.activity.MvvmActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.emcrp.webview.WebviewActivity
import com.gyf.immersionbar.ImmersionBar

class WxArticelActivity :
    MvvmActivity<ActivityWxarticleBinding, WxSearchResultViewModel, DataX>() {
    companion object {
        fun start(context: Context, key: String, cid: Int) {
            val intent = Intent(context, WxArticelActivity::class.java)
            intent.putExtra("key", key)
            intent.putExtra("cid", cid)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    lateinit var errView: View
    lateinit var emptyView: View
    lateinit var loadView: View
    lateinit var quickAdapter: BaseQuickAdapter<DataX, BaseDataBindingHolder<ListitemWxArticleBinding>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(false).navigationBarColor(R.color.colorPrimary)
            .statusBarColor(R.color.colorAccent).init()

        viewDataBinding.ivBack.setOnClickListener { finish() }
        viewDataBinding.ivSearch.setOnClickListener {
            viewDataBinding.srl.autoRefresh()
        }
        quickAdapter = object :
            BaseQuickAdapter<DataX, BaseDataBindingHolder<ListitemWxArticleBinding>>(R.layout.listitem_wx_article) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemWxArticleBinding>,
                item: DataX
            ) {
                holder.dataBinding?.model = item
            }
        }
        viewDataBinding.rcv.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rcv.adapter = quickAdapter
        quickAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (!TextUtils.isEmpty(quickAdapter.data[position].link))
                    WebviewActivity.startCommonWeb(
                        this@WxArticelActivity,
                        quickAdapter.data[position].title,
                        quickAdapter.data[position].link
                    )
            }
        })


        viewModel?.errorMessage?.observe(this, { s ->
            if (!TextUtils.isEmpty(s)) {
                quickAdapter.setEmptyView(errView)
                viewDataBinding.srl.finishRefresh()
                viewDataBinding.srl.finishLoadMore()
            }
        })
        viewDataBinding.srl.setOnRefreshListener { viewModel?.refresh(viewDataBinding.et.text.toString().trim()) }

        viewDataBinding.srl.setOnLoadMoreListener { viewModel?.loadNext(viewDataBinding.et.text.toString().trim()) }
        emptyView = getLayoutInflater().inflate(
            R.layout.layout_state_empty,
            viewDataBinding.srl.getParent() as ViewGroup,
            false
        )
        emptyView.setOnClickListener {
            viewDataBinding.srl.autoRefresh()
        }
        errView = getLayoutInflater().inflate(
            R.layout.layout_state_error,
            viewDataBinding.srl.getParent() as ViewGroup,
            false
        )

        loadView = getLayoutInflater().inflate(
            R.layout.layout_state_loading,
            viewDataBinding.srl.getParent() as ViewGroup,
            false
        )
        quickAdapter.setEmptyView(loadView)

    }

    override fun getViewModel(): WxSearchResultViewModel {
        if (viewModel == null) {
            viewModel = intent.getStringExtra("key")?.let {
                ViewModelProvider(this).get(WxSearchResultViewModel::class.java).init(
                    it, intent.getIntExtra("cid", 0)
                )
            }
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable() = 0

    override fun goToLogin() {     LoginActivity.start(this)}
    override fun onListItemInserted(sender: ObservableList<DataX>) {
        viewDataBinding.srl.finishRefresh()
        viewDataBinding.srl.finishLoadMore()
        quickAdapter.setList(sender)
        quickAdapter.setEmptyView(emptyView)
    }

    override fun getLayoutId() = R.layout.activity_wxarticle
}