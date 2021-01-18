package cn.laitt.wanandroid.module.home.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.commom.*
import cn.laitt.wanandroid.databinding.FragmentHomeBinding
import cn.laitt.wanandroid.model.Article
import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.module.home.ui.activity.SearchActivity
import cn.laitt.wanandroid.module.home.ui.view.NumIndicator
import cn.laitt.wanandroid.module.home.vm.*
import cn.laitt.wanandroid.module.knowledge.adapter.ArticleAdapter
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.common.utils.LiveDataBus
import com.arch.base.core.fragment.MvvmFragment
import com.arch.base.core.utils.ToastUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.emcrp.webview.WebviewActivity
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnBannerListener


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HomeFragment : MvvmFragment<FragmentHomeBinding, BannerViewModel, Banner.DataBean>(),
    SimpleImmersionOwner {
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)
    var list: MutableList<Article.DataBean.DatasBean>? = null
    lateinit var errView: View
    lateinit var emptyView: View
    var index = -1
    var articleViewModel: ArticleViewModel? = null
    var hotViewModel: HotArticleViewModel? = null
    var collectViewModel: CollectViewModel? = null
    var unCollectViewModel: UnCollectViewModel? = null
    override fun getLayoutId() = R.layout.fragment_home
    override fun getBindingVariable() = 0

    override fun getViewModel(): BannerViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(BannerViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun onListItemInserted(sender: ObservableList<Banner.DataBean>) {
        if (sender.size > 0) {
            viewDataBinding.banner.setAdapter(object : BannerImageAdapter<Banner.DataBean>(sender) {
                override fun onBindView(
                    holder: BannerImageHolder?,
                    data: Banner.DataBean,
                    position: Int,
                    size: Int
                ) {
                    //图片加载自己实现
                    Glide.with(holder!!.itemView)
                        .load(data.imagePath)
                        .centerCrop()
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .into(holder.imageView)
                }
            }).addBannerLifecycleObserver(this)
                .setIndicator(NumIndicator(this.context))
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                .setOnBannerListener(object : OnBannerListener<Banner.DataBean> {
                    override fun OnBannerClick(data: Banner.DataBean, position: Int) {
                        WebviewActivity.startCommonWeb(
                            this@HomeFragment.context,
                            data.title,
                            data.url
                        )
                    }
                })
        }
    }

    override fun getFragmentTag(): String {
        return javaClass.canonicalName
    }

    override fun goToLogin() {
        LoginActivity.start(requireContext())
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this).statusBarColorTransformEnable(false)
            .keyboardEnable(false).statusBarDarkFont(true).fitsSystemWindows(false)
            .navigationBarColor(R.color.colorPrimary)
            .init()
    }

    override fun immersionBarEnabled(): Boolean {
        return true
    }


    override fun onDestroy() {
        super<MvvmFragment>.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onHiddenChanged(hidden: Boolean) {
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super<MvvmFragment>.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super<MvvmFragment>.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
        viewDataBinding.tv.background.alpha = 80
        viewDataBinding.rcv.layoutManager = LinearLayoutManager(this.context)


        val quickAdapter = ArticleAdapter()
        viewDataBinding.rcv.adapter = quickAdapter
        hotViewModel = ViewModelProvider(this).get(HotArticleViewModel::class.java).init()
        collectViewModel = ViewModelProvider(this).get(CollectViewModel::class.java).init()
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java).init()
        unCollectViewModel = ViewModelProvider(this).get(UnCollectViewModel::class.java).init()
        lifecycle.addObserver(collectViewModel!!)
        lifecycle.addObserver(hotViewModel!!)
        lifecycle.addObserver(articleViewModel!!)
        lifecycle.addObserver(unCollectViewModel!!)
        hotViewModel?.viewStatusLiveData!!.observe(viewLifecycleOwner, this)
        collectViewModel?.viewStatusLiveData!!.observe(viewLifecycleOwner, this)
        articleViewModel?.viewStatusLiveData!!.observe(viewLifecycleOwner, this)
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
        hotViewModel?.dataList?.observe(this.viewLifecycleOwner, Observer {
            list?.clear()
            list = quickAdapter.data.filter {
                it.top == false
            }.toMutableList()
            quickAdapter.data.clear()
            quickAdapter.data.addAll(list!!)
            if (it.size > 0)
                quickAdapter.data.addAll(0, it)
            quickAdapter.notifyDataSetChanged()
        })
        articleViewModel?.dataList?.observe(this.viewLifecycleOwner, Observer {
            viewDataBinding.srl.finishLoadMore()
            viewDataBinding.srl.finishRefresh()
            list?.clear()
            list = quickAdapter.data.filter {
                it.top == true
            }.toMutableList()
            quickAdapter.data.clear()
            quickAdapter.data.addAll(list!!)
            quickAdapter.data.addAll(it)
            quickAdapter.notifyDataSetChanged()
        })
        articleViewModel?.errorMessage?.observe(this.viewLifecycleOwner, { s ->
            if (!TextUtils.isEmpty(s)) {
                viewDataBinding.srl.finishRefresh()
                viewDataBinding.srl.finishLoadMore()
            }

        })
        viewDataBinding.srl.setOnRefreshListener {
            articleViewModel?.refresh()
        }

        viewDataBinding.srl.setOnLoadMoreListener {
            articleViewModel?.loadNext()
        }
        viewDataBinding.tv.setOnClickListener { this.context?.let { it1 -> SearchActivity.start(it1) } }
        quickAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if (!TextUtils.isEmpty(quickAdapter.data[position].link))
                    WebviewActivity.startCommonWeb(
                        this@HomeFragment.context,
                        quickAdapter.data[position].title,
                        quickAdapter.data[position].link
                    )
            }
        })
        quickAdapter.setOnItemChildClickListener { _, _, position ->
            index = position
            if (quickAdapter.data[position].collect) {
                unCollectViewModel?.uncollectArticle(quickAdapter.data[position].id)
            } else
                collectViewModel?.collect(quickAdapter.data[position].id)
        }
        emptyView = getLayoutInflater().inflate(
            R.layout.layout_state_empty,
            viewDataBinding.rcv.getParent() as ViewGroup,
            false
        )
        errView = getLayoutInflater().inflate(
            R.layout.layout_state_error,
            viewDataBinding.rcv.getParent() as ViewGroup,
            false
        )

        LiveDataBus.get()
            .with(RELOGIN_KEY, Int::class.java)
            .observe(viewLifecycleOwner, {
                if (it == RELOGIN_VALUE) {
                    Log.w(tag, "onActivityCreated: 重新登录 刷新数据")
                    hotViewModel?.load()
                }
            })
        LiveDataBus.get()
            .with(LOGINOUT_KEY, Int::class.java)
            .observe(viewLifecycleOwner, {
                if (it == LOGINOUT_VALUE) {
                    Log.w(tag, "onActivityCreated: 登出 刷新数据")
                    hotViewModel?.load()
                }
            })
//        loadView = getLayoutInflater().inflate(
//            R.layout.layout_state_loading,
//            viewDataBinding.srl.getParent() as ViewGroup,
//            false
//        )
//        showLoading()
//        quickAdapter.setEmptyView(loadView)

    }
}