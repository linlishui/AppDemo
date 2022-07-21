package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import lishui.android.ui.widget.list.RecyclerEventMediator
import lishui.module.media.R
import lishui.module.media.ui.adapter.MediaListAdapter
import lishui.module.media.viewmodel.MediaViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */

abstract class LocalMediaListFragment : Fragment(R.layout.fragment_local_media_list) {

    companion object {
        internal const val TAB_INTERNAL = "Internal"
        internal const val TAB_EXTERNAL = "External"
    }

    private val mediaListAdapter = MediaListAdapter()

    private lateinit var loadingBar: ContentLoadingProgressBar
    internal lateinit var tabLayout: TabLayout
    internal lateinit var recyclerView: RecyclerView

    internal val mediaViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MediaViewModel::class.java)
    }

    internal val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
        backPressedCallback.isEnabled = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        subscribeToViewModel()
    }

    internal open fun initViews(root: View) {
        loadingBar = root.findViewById(R.id.media_content_loading)
        loadingBar.show()

        recyclerView = root.findViewById(R.id.media_browse_list)
        recyclerView.adapter = mediaListAdapter
        mediaListAdapter.setItemEventMediator(object : RecyclerEventMediator() {
            override fun onClick(v: View?) {
                val view = v ?: return
                onClickItem(view)
            }
        })

        tabLayout = root.findViewById(R.id.media_browse_tabs)
        tabLayout.addTab(tabLayout.newTab().setText(TAB_EXTERNAL))
        tabLayout.addTab(tabLayout.newTab().setText(TAB_INTERNAL))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.run {
                    when (this.text) {
                        TAB_EXTERNAL -> onExternalTabSelected()
                        TAB_INTERNAL -> onInternalTabSelected()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun subscribeToViewModel() {
        mediaViewModel.mediaModelList.observe(requireActivity()) {
            loadingBar.hide()
            mediaListAdapter.submitList(it)
        }
    }

    internal open fun onBackPressed() {
        activity?.finish()
    }

    internal open fun onInternalTabSelected() {
        loadingBar.show()
    }

    internal open fun onExternalTabSelected() {
        loadingBar.show()
    }

    internal open fun onClickItem(v: View) {
    }

    abstract fun loadData()
}