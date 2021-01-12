package cn.laitt.wanandroid.module.question.ui.vm

import cn.laitt.wanandroid.model.Article
import com.arch.base.core.viewmodel.MvvmBaseViewModel

class QuestionViewModel : MvvmBaseViewModel<QuestionModel, Article.DataBean.DatasBean>() {
    fun init(): QuestionViewModel {
        model = QuestionModel()
        model.register(this)
        model.getCachedDataAndLoad()
        return this
    }


    fun loadNext() {
        model.loadNext()
    }
}