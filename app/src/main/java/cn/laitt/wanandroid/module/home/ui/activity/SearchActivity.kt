package cn.laitt.wanandroid.module.home.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProvider
import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.ActivitySearchBinding
import cn.laitt.wanandroid.databinding.ListitemHistoryKeyBinding
import cn.laitt.wanandroid.databinding.ListitemHotkeyBinding
import cn.laitt.wanandroid.db.model.SearchHistroy
import cn.laitt.wanandroid.db.vm.SearchHistroyViewModel
import cn.laitt.wanandroid.model.Hotkey
import cn.laitt.wanandroid.module.home.vm.SearchViewModel
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import com.arch.base.core.activity.MvvmActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.flyco.dialog.listener.OnBtnClickL
import com.flyco.dialog.widget.MaterialDialog
import com.google.android.flexbox.FlexboxLayoutManager
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.*


class SearchActivity : MvvmActivity<ActivitySearchBinding, SearchViewModel, Hotkey.DataBean>(),
    CoroutineScope by MainScope() {
    private lateinit var mHotAdapter: BaseQuickAdapter<Hotkey.DataBean, BaseDataBindingHolder<ListitemHotkeyBinding>>
    private lateinit var mHistoryAdapter: BaseQuickAdapter<SearchHistroy, BaseDataBindingHolder<ListitemHistoryKeyBinding>>
    private var mRemoveMode = false
    private var mRemoveModeChanging = false
    private var searchHistroyViewModel: SearchHistroyViewModel? = null


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(false).navigationBarColor(R.color.colorPrimary)
            .statusBarColor(R.color.colorAccent).init()

        viewDataBinding.ivBack.setOnClickListener { finish() }
        viewDataBinding.ivSearch.setOnClickListener {
            if (!TextUtils.isEmpty(viewDataBinding.et.text.toString().trim())) {
                launch {
                    withContext(Dispatchers.IO) {
                        searchHistroyViewModel?.insert(
                            SearchHistroy(viewDataBinding.et.text.toString())
                        )
                    }
                    SearchResultActivity.start(
                        this@SearchActivity,
                        viewDataBinding.et.text.toString()
                    )
                }
            }
        }
        viewDataBinding.tvClean.setOnClickListener { tipDialog() }
        viewDataBinding.tvDown.setOnClickListener { changeRemoveMode(false) }

        viewDataBinding.rvHot.setNestedScrollingEnabled(false)
        viewDataBinding.rvHot.setHasFixedSize(true)
        viewDataBinding.rvHot.setLayoutManager(FlexboxLayoutManager(this))

        viewDataBinding.rvHistory.setNestedScrollingEnabled(false)
        viewDataBinding.rvHistory.setHasFixedSize(true)
        viewDataBinding.rvHistory.setLayoutManager(FlexboxLayoutManager(this))

        mHotAdapter = object :
            BaseQuickAdapter<Hotkey.DataBean, BaseDataBindingHolder<ListitemHotkeyBinding>>(R.layout.listitem_hotkey) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemHotkeyBinding>,
                item: Hotkey.DataBean
            ) {
                holder.dataBinding!!.tvName.text = item.name
            }
        }
        viewDataBinding.rvHot.adapter = mHotAdapter
        mHotAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                launch {
                    withContext(Dispatchers.IO) {
                        searchHistroyViewModel?.insert(
                            SearchHistroy(mHotAdapter.getItem(position).name!!)
                        )
                    }
                    SearchResultActivity.start(
                        this@SearchActivity,
                        mHotAdapter.getItem(position).name!!
                    )
                }
            }
        })
        mHistoryAdapter = object :
            BaseQuickAdapter<SearchHistroy, BaseDataBindingHolder<ListitemHistoryKeyBinding>>(R.layout.listitem_history_key) {
            override fun convert(
                holder: BaseDataBindingHolder<ListitemHistoryKeyBinding>,
                item: SearchHistroy
            ) {
                holder.dataBinding!!.tvKey.text = item.name
                holder.dataBinding!!.ivRemove.setOnClickListener {
                    searchHistroyViewModel?.delete(item.name)
                }
                if (!mRemoveModeChanging) {
                    holder.dataBinding!!.ivRemove.visibility =
                        if (mRemoveMode) View.VISIBLE else View.GONE
                } else {
                    if (mRemoveMode) {
                        val scaleAnimation = ScaleAnimation(
                            0f, 1f, 0f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                        )
                        scaleAnimation.duration = 300
                        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {
                                holder.dataBinding!!.ivRemove.visibility = View.VISIBLE
                            }

                            override fun onAnimationEnd(animation: Animation) {
                                mRemoveModeChanging = false
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                        holder.dataBinding!!.ivRemove.startAnimation(scaleAnimation)
                    } else {
                        val scaleAnimation = ScaleAnimation(
                            1f, 0f, 1f, 0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                        )
                        scaleAnimation.duration = 300
                        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {
                                mRemoveModeChanging = false
                                holder.dataBinding!!.ivRemove.visibility = View.INVISIBLE
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        })
                        holder.dataBinding!!.ivRemove.startAnimation(scaleAnimation)
                    }
                }
            }
        }

        mHistoryAdapter.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ): Boolean {
                changeRemoveMode(!mRemoveMode)
                return true
            }
        })
        mHistoryAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                SearchResultActivity.start(
                    this@SearchActivity,
                    mHistoryAdapter.getItem(position).name
                )
            }
        })
        viewDataBinding.rvHistory.adapter = mHistoryAdapter
        searchHistroyViewModel = ViewModelProvider(this).get(SearchHistroyViewModel::class.java)
        searchHistroyViewModel?.getliveDataSearchHistroy()?.observe(this, { datas ->
            mHistoryAdapter.setList(datas)
        })
    }

    private fun tipDialog() {
        val dialog = MaterialDialog(this)
        dialog.content(
            "确定要清除搜索历史？"
        ) //
            .btnText("取消", "确定") //
//            .showAnim(mBasIn) //
//            .dismissAnim(mBasOut) //
            .show()
        dialog.setOnBtnClickL(
            OnBtnClickL
            //left btn click listener
            {
                dialog.dismiss()
            },
            OnBtnClickL
            //right btn click listener
            {
                searchHistroyViewModel?.deleteAll()
                dialog.dismiss()
            }
        )
    }

    private fun changeRemoveMode(removeMode: Boolean) {
        if (mRemoveMode == removeMode) {
            return
        }
        mRemoveModeChanging = true
        mRemoveMode = removeMode
        mHistoryAdapter.notifyDataSetChanged()
        if (removeMode) {
            viewDataBinding.tvDown.visibility = View.VISIBLE
            viewDataBinding.tvClean.visibility = View.GONE
        } else {
            viewDataBinding.tvDown.visibility = View.GONE
            viewDataBinding.tvClean.visibility = View.VISIBLE
        }
    }

    override fun getViewModel(): SearchViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(SearchViewModel::class.java).init()
            return viewModel
        }
        return viewModel
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun goToLogin() {
        LoginActivity.start(this)
    }

    override fun onListItemInserted(sender: ObservableList<Hotkey.DataBean>?) {
        mHotAdapter.setList(sender)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}