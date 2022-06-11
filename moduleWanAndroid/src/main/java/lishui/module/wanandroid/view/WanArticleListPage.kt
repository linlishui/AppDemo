package lishui.module.wanandroid.view

import android.content.Context
import android.lib.base.BaseActivity
import android.lib.base.log.LogUtils
import android.lib.base.util.CommonUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lishui.android.ui.extensions.dp
import lishui.android.ui.widget.floating.AbstractFloatingView
import lishui.lib.router.core.Router
import lishui.module.wanandroid.R
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.ui.recyclerview.adapter.WanArticleAdapter
import lishui.service.core.router.RouterPath

/**
 * @author lishui.lin
 * Created it on 2021/8/17
 */
class WanArticleListPage(
    context: Context,
    attrs: AttributeSet? = null
) : AbstractFloatingView(context, attrs), View.OnClickListener {

    private val mAdapter: WanArticleAdapter = WanArticleAdapter()

    init {
        // retrieve this view attributes
        attrs?.run {
            val childCount = attrs.attributeCount
            for (i in 0 until childCount) {
                val name = attrs.getAttributeName(i)
                val value = attrs.getAttributeValue(i)
                LogUtils.d("WanTreeArticlesPage attr {name=$name, value=$value}")
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    override fun handleClose(animate: Boolean) {
        mIsOpen = false

        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }
    }

    override fun isOfType(type: Int): Boolean = type and TYPE_ACTION_POPUP_LIST != 0

    override fun onClick(v: View?) {
        val tag = v?.tag
        if (tag is Article) {
            val postcard = Router.getInstance().build(RouterPath.Web.BROWSER)
            postcard.extras.putString(RouterPath.EXTRA_WEB_URL, tag.link)
            postcard.navigation()
        }
    }

    fun open() {
        val openWanPage = getOpenView<WanArticleListPage>(context, TYPE_ACTION_POPUP_LIST)
        if (openWanPage != null && openWanPage != this) {
            openWanPage.close(false)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.wan_tree_article_list)
        recyclerView.run {
            this.adapter = mAdapter
            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            this.layoutManager = LinearLayoutManager(context)
            mAdapter.setClickListener(this@WanArticleListPage)

            mAdapter.addLoadStateListener {
                LogUtils.d("WanTreeArticlesPage", "Lading state=${it.refresh}")
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        this.visibility = View.VISIBLE
                    }
                    is LoadState.Loading -> {
                        this.visibility = View.INVISIBLE
                    }
                    is LoadState.Error -> {
                        val state = it.refresh as LoadState.Error
                        Toast.makeText(
                            context,
                            "Load Error: ${state.error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        mIsOpen = true

        if (parent == null) {
            val rootView = CommonUtils.findActivity<BaseActivity>(context).decorView as ViewGroup

            rootView.addView(
                this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val layoutParams = this.layoutParams as MarginLayoutParams
            layoutParams.topMargin = dp(24)
            layoutParams.bottomMargin = dp(48)
            this.layoutParams = layoutParams
        }
    }

    suspend fun submitData(value: PagingData<Article>) {
        if (isOpen) {
            mAdapter.submitData(value)
        }
    }

    companion object {

        fun fromXml(context: Context): WanArticleListPage = LayoutInflater.from(context).inflate(
            R.layout.view_wan_tree_detail_layout, null
        ) as WanArticleListPage
    }

}