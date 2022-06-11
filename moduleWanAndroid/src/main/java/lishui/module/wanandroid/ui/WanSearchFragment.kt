package lishui.module.wanandroid.ui

import android.lib.base.log.LogUtils
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lishui.lib.router.core.Router
import lishui.module.wanandroid.R
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.ui.recyclerview.adapter.WanArticleAdapter
import lishui.module.wanandroid.view.SearchBar
import lishui.module.wanandroid.viewmodel.WanAndroidViewModel
import lishui.service.core.router.RouterPath

/**
 * @author lishui.lin
 * Created it on 2021/8/16
 */
class WanSearchFragment : Fragment(R.layout.fragment_wan_search_layout), View.OnClickListener {

    private var mSearchKeyWord = ""
    private val mSearchAdapter = WanArticleAdapter()

    private lateinit var mViewModel: WanAndroidViewModel

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        mRecyclerView = view.findViewById(R.id.search_list)
        mProgressBar = view.findViewById(R.id.progress_bar)

        val searchBar: SearchBar = view.findViewById(R.id.search_bar)
        searchBar.onSearchListener = {
            it?.trim()?.apply {
                mSearchKeyWord = this
            }
        }
        searchBar.onImeSearchActionListener = {
            lifecycleScope.launch {
                mViewModel.searchKeyword(mSearchKeyWord).collect {
                    mSearchAdapter.submitData(it)
                }
            }
        }

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mSearchAdapter
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        mSearchAdapter.setClickListener(this)

        mSearchAdapter.addLoadStateListener {
            LogUtils.d(TAG, "Lading state=${it.refresh}")
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    mProgressBar.hide()
                    mRecyclerView.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    mProgressBar.show()
                    mRecyclerView.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    mProgressBar.hide()
                    Toast.makeText(
                        context,
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun subscribeToViewModel() {
        mViewModel = ViewModelProvider(requireActivity()).get(WanAndroidViewModel::class.java)
    }

    override fun onClick(v: View?) {
        val tag = v?.tag
        if (tag is Article) {
            val postcard = Router.getInstance().build(RouterPath.Web.BROWSER)
            postcard.extras.putString(RouterPath.EXTRA_WEB_URL, tag.link)
            postcard.navigation()
        }
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}