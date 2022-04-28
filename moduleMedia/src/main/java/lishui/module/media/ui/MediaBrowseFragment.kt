package lishui.module.media.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import lishui.lib.base.log.LogUtils
import lishui.module.media.R
import lishui.module.media.viewmodel.MediaViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
private const val TAB_INTERNAL = "Internal"
private const val TAB_EXTERNAL = "External"

abstract class MediaBrowseFragment : Fragment(R.layout.fragment_media_browze), View.OnClickListener {

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
        recyclerView.adapter =
            mediaListAdapter.also { it.setClickListener(this@MediaBrowseFragment) }

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
            mediaListAdapter.updateItems(it)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            LogUtils.d(it.tag.toString())
            onClickItem(it)
        }
    }

    internal open fun onBackPressed() {
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