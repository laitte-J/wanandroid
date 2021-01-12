package cn.laitt.wanandroid.module.knowledge.adapter

import cn.laitt.wanandroid.R
import cn.laitt.wanandroid.databinding.ListitemArticleTopBinding
import cn.laitt.wanandroid.model.Article
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import org.jetbrains.annotations.NotNull


class ArticleAdapter :
    BaseQuickAdapter<Article.DataBean.DatasBean, BaseDataBindingHolder<ListitemArticleTopBinding>>(R.layout.listitem_article_top) {
    init {
        addChildClickViewIds(R.id.iv_keep)
    }

    override fun convert(
        @NotNull holder: BaseDataBindingHolder<ListitemArticleTopBinding>,
        item: Article.DataBean.DatasBean
    ) {
        val binding = holder.dataBinding
        if (binding != null) {
            binding.model = item
            binding.ivKeep.isActivated = item.collect
            binding.executePendingBindings()
        }
    }
}