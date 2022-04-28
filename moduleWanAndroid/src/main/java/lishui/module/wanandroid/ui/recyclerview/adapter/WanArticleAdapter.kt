package lishui.module.wanandroid.ui.recyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.ui.recyclerview.viewholder.WanArticleViewHolder

/**
 *  author : linlishui
 *  time   : 2020/11/12
 *  desc   : WanAndroid中文章类型的适配器
 */
class WanArticleAdapter : PagingDataAdapter<Article, WanArticleViewHolder>(diffCallback) {

    private var clickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WanArticleViewHolder {
        return WanArticleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: WanArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, clickListener)
    }

    fun setClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                // Id is unique.
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}


