package cn.laitt.wanandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableList
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.NavHostFragment
import cn.laitt.wanandroid.commom.FixFragmentNavigator
import cn.laitt.wanandroid.databinding.ActivityMainBinding
import cn.laitt.wanandroid.module.home.ui.fragment.HomeFragment
import cn.laitt.wanandroid.module.login.ui.LoginActivity
import cn.laitt.wanandroid.module.mine.ui.fragment.KnowledgeFragment
import cn.laitt.wanandroid.module.mine.ui.fragment.MineFragment
import cn.laitt.wanandroid.module.question.ui.fragment.QuestionFragment
import com.arch.base.core.activity.MvvmActivity
import com.arch.base.core.utils.ToastUtil
import com.arch.base.core.viewmodel.MvvmBaseViewModel
import com.emcrp.network.beans.BaseResponse

class MainActivity() :
    MvvmActivity<ActivityMainBinding, MvvmBaseViewModel<*, *>?, BaseResponse>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    var time: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch(Manifest.permission.READ_CALENDAR)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        //获取导航控制器
        val navController = NavHostFragment.findNavController(fragment!!)
        //创建自定义的Fragment导航器
        val fragmentNavigator =
            FixFragmentNavigator(
                this,
                fragment.childFragmentManager,
                fragment.id
            )
        //获取导航器提供者
        val provider = navController.navigatorProvider
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fragmentNavigator)
        //手动创建导航图
        val navGraph: NavGraph = initNavGraph(provider, fragmentNavigator)
        //设置导航图
        navController.graph = navGraph
        viewDataBinding.navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }
        viewDataBinding.navView.  setItemIconTintList(null);
        viewDataBinding.navView.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.bnav_btn_text_selector)
    }

    private fun initNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))
        //用自定义的导航器来创建目的地
        val home = fragmentNavigator.createDestination()
        home.id = R.id.navigation_home
        home.className = HomeFragment::class.java.getCanonicalName()
        home.label = getString(R.string.home)
        navGraph.addDestination(home)
        val question = fragmentNavigator.createDestination()
        question.id = R.id.navigation_question
        question.className = QuestionFragment::class.java.getCanonicalName()
        question.label = getString(R.string.question)
        navGraph.addDestination(question)
        val knowledge = fragmentNavigator.createDestination()
        knowledge.id = R.id.navigation_knowledge
        knowledge.className = KnowledgeFragment::class.java.getCanonicalName()
        knowledge.label = getString(R.string.knowledge)
        navGraph.addDestination(knowledge)
        val mine = fragmentNavigator.createDestination()
        mine.id = R.id.navigation_mine
        mine.className = MineFragment::class.java.getCanonicalName()
        mine.label = getString(R.string.mine)
        navGraph.addDestination(mine)
        navGraph.startDestination = R.id.navigation_home
        return navGraph
    }

    override fun getViewModel(): MvvmBaseViewModel<*, *>? {
        return null
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun goToLogin() {
        TODO("Not yet implemented")
    }

    override fun onListItemInserted(sender: ObservableList<BaseResponse>?) {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    private fun exitApp() {
        if (System.currentTimeMillis() - time < 3000) {
            finish()
        } else {
            time = System.currentTimeMillis()
            ToastUtil.show("再按一次退出应用！")
        }
        Handler().postDelayed({ time = 0 }, 3000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}