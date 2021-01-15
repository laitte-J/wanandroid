package cn.laitt.wanandroid.module.project.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.FragmentTreeListBinding
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.module.home.vm.CollectViewModel
import cn.laitt.wanandroid.module.home.vm.UnCollectViewModel
import cn.laitt.wanandroid.module.knowledge.adapter.ArticleAdapter
import cn.laitt.wanandroid.module.knowledge.ui.fragment.TreeListFragment
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import cn.laitt.wanandroid.module.project.vm.ProjectListViewModel
import com.arch.base.core.fragment.MvvmFragment
import com.arch.base.core.utils.ToastUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.emcrp.webview.WebviewActivity

class ProjectListFragment :
    MvvmFragment<FragmentTreeListBinding, ProjectListViewModel, Article.DataBean.DatasBean>() {
    lateinit var quickAdapter: ArticleAdapter
    lateinit var errView: View
    lateinit var emptyView: View
    lateinit var loadView: View
    var cid: Int? = 0
    var index = -1
    var collectViewModel: CollectViewModel? = null
    var unCollectViewModel: UnCollectViewModel? = null

    companion object {
        fun create(cid: Int): TreeListFragment {
            val fragment =
                TreeListFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) cid = arguments?.getInt("cid")
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.rcv.layoutManager = LinearLayoutManager(context)
        quickAdapter = ArticleAdapter()
        viewDataBinding.rcv.adapter = quickAdapter
        quickAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (!TextUtils.isEmpty(quickAdapter.data[position].link))
                    WebviewActivity.startCommonWeb(
                        context,
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
        collectViewModel = ViewModelProvider(this).get(CollectViewModel::class.java).init()
        unCollectViewModel = ViewModelProvider(this).get(UnCollectViewModel::class.java).init()
        lifecycle.addObserver(collectViewModel!!)
        lifecycle.addObserver(unCollectViewModel!!)
        collectViewModel?.viewStatusLiveData!!.observe(viewLifecycleOwner, this)
        unCollectViewModel?.viewStatusLiveData!!.observe(viewLifecycleOwner, this)

        collectViewModel?.dataList?.observe(viewLifecycleOwner, {
            if (it != null && it.size > 0) {
                quickAdapter.data[index].collect = true
                quickAdapter.notifyItemChanged(index)
            }
        })
        unCollectViewModel?.dataList?.observe(viewLifecycleOwner, {
            if (it != null && it.size > 0) {
                quickAdapter.data[index].collect = false
                quickAdapter.notifyItemChanged(index)
            }
        })

        collectViewModel?.errorMessage?.observe(viewLifecycleOwner, {
            ToastUtil.show(it)
        })
        unCollectViewModel?.errorMessage?.observe(viewLifecycleOwner, {
            ToastUtil.show(it)
        })

        viewModel?.errorMessage?.observe(viewLifecycleOwner, { s ->
            if (!TextUtils.isEmpty(s)) {
                quickAdapter.setEmptyView(errView)
                viewDataBinding.srl.finishRefresh()
                viewDataBinding.srl.finishLoadMore()
            }
        })
        viewDataBinding.srl.setOnRefreshListener { viewModel?.tryToRefresh() }

        viewDataBinding.srl.setOnLoadMoreListener { viewModel?.loadNext() }
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

    override fun getViewModel(): ProjectListViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(ProjectListViewModel::class.java).init(cid!!)
            return viewModel
        }
        return viewModel
    }

    override fun onListItemInserted(sender: ObservableList<Article.DataBean.DatasBean>) {
        viewDataBinding.srl.finishLoadMore()
        viewDataBinding.srl.finishRefresh()
        quickAdapter.setList(sender)
        quickAdapter.setEmptyView(emptyView)
    }

    override fun getFragmentTag(): String {
        return this.javaClass.name
    }

    override fun goToLogin() {
        LoginActivity.start(requireContext())
    }
}

