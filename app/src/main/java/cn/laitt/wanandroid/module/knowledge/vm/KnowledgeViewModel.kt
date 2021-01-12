package cn.laitt.wanandroid.module.knowledge.vm

import cn.laitt.wanandroid.model.Banner
import cn.laitt.wanandroid.model.Tree
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class KnowledgeViewModel : MvvmBaseViewModel<KnowledgeModel, Tree>() {
    fun init(): KnowledgeViewModel {
        model = KnowledgeModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }
}