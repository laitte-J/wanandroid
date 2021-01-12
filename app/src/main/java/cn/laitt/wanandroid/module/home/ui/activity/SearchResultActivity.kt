package cn.laitt.wanandroid.module.home.ui.activity

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
import cn.laitt.wanandroid.databinding.ActivitySearchReslutBinding
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.module.home.vm.CollectViewModel
import cn.laitt.wanandroid.module.home.vm.SearchResultViewModel
import cn.laitt.wanandroid.module.home.vm.UnCollectViewModel
import cn.laitt.wanandroid.module.knowledge.adapter.ArticleAdapter
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.emcrp.webview.WebviewActivity
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * 搜索结果
 */
class SearchResultActivity :
    MvvmActivity<ActivitySearchReslutBinding, SearchResultViewModel, Article.DataBean.DatasBean>() {
    lateinit var errView: View
    lateinit var emptyView: View
    lateinit var loadView: View
    lateinit var quickAdapter: ArticleAdapter
    var collectViewModel: CollectViewModel? = null
    var unCollectViewModel: UnCollectViewModel? = null
    var index = -1

    companion object {
        fun start(context: Context, key: String) {
            val intent = Intent(context, SearchResultActivity::class.java)
            intent.putExtra("key", key)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary)
            .init();
        viewDataBinding.ivBack.setOnClickListener { finish() }
        viewDataBinding.tvTitle.apply { text = intent.getStringExtra("key")!! }

        viewDataBinding.rcv.layoutManager = LinearLayoutManager(this)
        quickAdapter = ArticleAdapter()

        viewDataBinding.rcv.adapter = quickAdapter
        quickAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (!TextUtils.isEmpty(quickAdapter.data[position].link))
                    WebviewActivity.startCommonWeb(
                        this@SearchResultActivity,
                        quickAdapter.data[position].title,
                        quickAdapter.data[position].link
                    )
            }
        })
        quickAdapter.setOnItemChildClickListener { adapter, view, position ->
            index = position
            if (quickAdapter.data[position].collect) {
                unCollectViewModel?.uncollectArticle(quickAdapter.data[position].id)
            } else
                collectViewModel?.collect(quickAdapter.data[position].id)
        }

        viewModel?.errorMessage?.observe(this, { s ->
            if (!TextUtils.isEmpty(s)) {
                quickAdapter.setEmptyView(errView)
                viewDataBinding.srl.finishRefresh()
                viewDataBinding.srl.finishLoadMore()
            }
        })
        viewDataBinding.srl.setOnRefreshListener { viewModel?.refresh(intent.getStringExtra("key")!!) }
        viewDataBinding.srl.setOnLoadMoreListener { viewModel?.loadNext(intent.getStringExtra("key")!!) }

        collectViewModel = ViewModelProvider(this).get(CollectViewModel::class.java).init()
        unCollectViewModel = ViewModelProvider(this).get(UnCollectViewModel::class.java).init()
        lifecycle.addObserver(collectViewModel!!)
        lifecycle.addObserver(unCollectViewModel!!)
        collectViewModel?.viewStatusLiveData!!.observe(this, this)
        unCollectViewModel?.viewStatusLiveData!!.observe(this, this)

        collectViewModel?.dataList?.observe(this, {
            if (it != null && it.size > 0) {
                quickAdapter.data[index].collect = true
                quickAdapter.notifyItemChanged(index)
            }
        })
        unCollectViewModel?.dataList?.observe(this, {
            if (it != null && it.size > 0) {
                quickAdapter.data[index].collect = false
                quickAdapter.notifyItemChanged(index)
            }
        })

        collectViewModel?.errorMessage?.observe(this, {
            ToastUtil.show(it)
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


    override fun getViewModel(): SearchResultViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(SearchResultViewModel::class.java)
                .init(intent.getStringExtra("key")!!)
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

    override fun getLayoutId() = R.layout.activity_search_reslut

}