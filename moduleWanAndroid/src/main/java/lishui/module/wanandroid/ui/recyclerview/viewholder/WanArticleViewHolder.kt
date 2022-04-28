package lishui.module.wanandroid.ui.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.R

/**
 * @author lishui.lin
 * Created it on 21-4-23
 */
class WanArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_wan_article_layout, parent, false)
) {
    private val articleTitle: TextView = itemView.findViewById(R.id.wan_article_title)
    private val articleDate: TextView = itemView.findViewById(R.id.wan_article_date)
    private val articleChapter: TextView = itemView.findViewById(R.id.wan_article_chapter)

    fun bind(article: Article?, clickListener: View.OnClickListener?) {
        articleTitle.text = HtmlCompat.fromHtml(
            article?.title ?: "",
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
        )
        articleChapter.text = article?.superChapterName ?: ""
        articleDate.text = article?.niceDate ?: ""
        itemView.tag = article
        itemView.setOnClickListener(clickListener)
    }
}