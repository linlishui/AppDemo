package lishui.module.wanandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.flexbox.FlexboxLayoutManager
import lishui.module.wanandroid.R
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem
import lishui.module.wanandroid.ui.recyclerview.adapter.WanNavTreeAdapter

/**
 *  author : linlishui
 *  time   : 2021/8/17
 *  desc   : WanAndroid中导航与体系的父fragment
 */
abstract class WanNavTreeFragment : WanBackFragment(), View.OnClickListener {

    private val mNavTreeAdapter: WanNavTreeAdapter by lazy {
        WanNavTreeAdapter().also {
            it.itemClickListener = this
        }
    }

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private var mLastClickViewTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(context).inflate(
        R.layout.fragment_wanandroid_nav_tree, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeRefreshLayout = view.findViewById(R.id.wan_swipe_refresh_layout)
        mSwipeRefreshLayout.isRefreshing = true
        mSwipeRefreshLayout.setOnRefreshListener {
            loadData()
        }

        view.findViewById<RecyclerView>(R.id.wan_nav_tree_list).also {
            it.adapter = mNavTreeAdapter
            val layoutManager = FlexboxLayoutManager(context)
            it.layoutManager = layoutManager
        }

        subscribeViewModel()
    }

    override fun onClick(v: View?) {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - mLastClickViewTime > 500) {
            mLastClickViewTime = currentClickTime
            if (v != null) {
                onClickView(v)
            }
        }
    }

    fun updateItemList(itemList: List<WanNavTreeItem>) {
        mSwipeRefreshLayout.isRefreshing = false
        mNavTreeAdapter.submitList(itemList)
    }

    abstract fun loadData()

    abstract fun subscribeViewModel()

    abstract fun onClickView(v: View)

}