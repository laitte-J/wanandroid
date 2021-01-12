package cn.laitt.wanandroid.module.mine.ui.activity

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
import cn.laitt.wanandroid.databinding.ActivityCollectListBinding
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.module.home.vm.CollectViewModel
import cn.laitt.wanandroid.module.home.vm.UnCollectViewModel
import cn.laitt.wanandroid.module.knowledge.adapter.ArticleAdapter
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import cn.laitt.wanandroid.module.mine.vm.CollectListViewModel
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.emcrp.webview.WebviewActivity
import com.gyf.immersionbar.ImmersionBar

class CollectListActivity :
    MvvmActivity<ActivityCollectListBinding, CollectListViewModel, Article.DataBean.DatasBean>() {
    lateinit var errView: View
    lateinit var emptyView: View
    lateinit var loadView: View
    lateinit var quickAdapter: ArticleAdapter
    var unCollectViewModel: UnCollectViewModel? = null
    var index = -1

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .init()
        viewDataBinding.ivBack.setOnClickListener { finish() }

        viewDataBinding.rcv.layoutManager = LinearLayoutManager(this)
        quickAdapter = ArticleAdapter()

        viewDataBinding.rcv.adapter = quickAdapter
        quickAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (!TextUtils.isEmpty(quickAdapter.data[position].link))
                    WebviewActivity.startCommonWeb(
                        this@CollectListActivity,
                        quickAdapter.data[position].title,
                        quickAdapter.data[position].link
                    )
            }
        })
        quickAdapter.setOnItemChildClickListener { adapter, view, position ->
            index = position
            unCollectViewModel?.uncollectArticle(quickAdapter.data[position].id,quickAdapter.data[position].originId)
        }

        viewModel?.errorMessage?.observe(this, { s ->
            if (!TextUtils.isEmpty(s)) {
                quickAdapter.setEmptyView(errView)
                viewDataBinding.srl.finishRefresh()
                viewDataBinding.srl.finishLoadMore()
            }
        })
        viewDataBinding.srl.setOnRefreshListener { viewModel?.tryToRefresh() }
        viewDataBinding.srl.setOnLoadMoreListener { viewModel?.loadNext() }
        unCollectViewModel = ViewModelProvider(this).get(UnCollectViewModel::class.java).init()
        lifecycle.addObserver(unCollectViewModel!!)
        unCollectViewModel?.viewStatusLiveData!!.observe(this, this)


        unCollectViewModel?.dataList?.observe(this, {
            if (it != null && it.size > 0) {
                quickAdapter.data[index].collect = false
                quickAdapter.removeAt(index)
            }
        })


        unCollectViewModel?.errorMessage?.observe(this, {
            ToastUtil.show(it)
        })


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


    override fun getViewModel(): CollectListViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(CollectListViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable() = 0

    override fun goToLogin() {
        LoginActivity.start(this)
    }

    override fun onListItemInserted(sender: ObservableList<Article.DataBean.DatasBean>?) {
        viewDataBinding.srl.finishLoadMore()
        viewDataBinding.srl.finishRefresh()
        quickAdapter.setList(sender)
        quickAdapter.setEmptyView(emptyView)
    }

    override fun getLayoutId() = R.layout.activity_collect_list

}