package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import lishui.android.ui.widget.list.RecyclerEventMediator
import lishui.module.media.R
import lishui.module.media.ui.adapter.MediaListAdapter
import lishui.module.media.viewmodel.MediaViewModel

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 网络媒体列表 - 基类
 */
abstract class NetMediaListFragment : Fragment(R.layout.fragment_net_media_list) {
    private val mediaListAdapter = MediaListAdapter()

    internal lateinit var recyclerView: RecyclerView
    private lateinit var loadingBar: ContentLoadingProgressBar

    internal val mediaViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MediaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        subscribeToViewModel()
    }

    internal open fun initViews(root: View) {
        loadingBar = root.findViewById(R.id.content_loading)
        loadingBar.show()

        recyclerView = root.findViewById(R.id.media_list)
        recyclerView.adapter = mediaListAdapter
        mediaListAdapter.setItemEventMediator(object : RecyclerEventMediator() {
            override fun onClick(v: View?) {
                val view = v ?: return
                onClickItem(view)
            }
        })
    }

    private fun subscribeToViewModel() {
        mediaViewModel.mediaModelList.observe(requireActivity()) {
            loadingBar.hide()
            mediaListAdapter.submitList(it)
        }
    }

    internal open fun onClickItem(v: View) {}

    abstract fun loadData()
}