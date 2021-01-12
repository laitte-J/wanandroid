package cn.laitt.wanandroid

import android.app.Application
import android.content.Context
import com.arch.base.core.preference.PreferencesUtil
import com.arch.base.core.utils.ToastUtil
import com.blankj.utilcode.util.AppUtils
import com.emcrp.network.WanAndroidApi
import com.emcrp.network.base.INetworkRequiredInfo
import com.emcrp.network.base.NetworkApi
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator


class App : Application() {

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {
            override fun createRefreshHeader(
                context: Context,
                layout: RefreshLayout
            ): RefreshHeader {
                layout.setPrimaryColorsId(R.color.white, R.color.white)//全局设置主题颜色
                return ClassicsHeader(context)

            }
        })
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(
                context: Context,
                layout: RefreshLayout
            ): RefreshFooter {
                return ClassicsFooter(context).setDrawableSize(20f)
            }
        })
    }

    override fun onCreate() {
        super.onCreate()
        NetworkApi.init(object : INetworkRequiredInfo {
            override fun getAppVersionName(): String {
                return AppUtils.getAppVersionName()
            }

            override fun getAppVersionCode(): String {
                return AppUtils.getAppVersionCode().toString() + ""
            }

            override fun isDebug(): Boolean {
                return true
            }

            override fun getApplicationContext(): Application {
                return this@App
            }
        })
        PreferencesUtil.init(this)
        ToastUtil.init(this)
    }
}