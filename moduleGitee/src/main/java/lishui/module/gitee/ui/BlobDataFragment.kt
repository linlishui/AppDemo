package lishui.module.gitee.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import lishui.android.ui.extensions.hidden
import lishui.lib.base.log.LogUtils
import lishui.module.gitee.R
import lishui.module.gitee.viewmodel.GiteeViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */

class BlobDataFragment : GiteeFragment() {

    private lateinit var repoName: String
    private lateinit var content: TextView
    private lateinit var contentLoading: ContentLoadingProgressBar

    private lateinit var viewModel: GiteeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GiteeViewModel::class.java)

        requireArguments().apply {
            val repoOwner = getString(REPO_OWNER, "")
            repoName = getString(REPO_NAME, "")
            val repoSha = getString(REPO_SHA, "")
            if (repoOwner.isNotBlank() && repoName.isNotBlank() && repoSha.isNotBlank()) {
                viewModel.requestFileContent(repoOwner, repoName, repoSha)
            } else {
                throw IllegalStateException("BlobDataFragment can not find valid args in onCreate")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gitee_blob_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置TextView内容可滑动
        content = view.findViewById(R.id.blob_content)
        content.movementMethod = ScrollingMovementMethod.getInstance()
        contentLoading = view.findViewById(R.id.blob_content_loading)
        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.blobContentLiveData.observe(requireActivity()) {
            content.text = it
            content.hidden = false
            contentLoading.hide()
            LogUtils.d("subscribeToViewModel repo name=$repoName")
        }
    }

    override fun onBackPressed() {
        activity?.run {
            if (this is GiteeActivity) {
                this.switchFragment(GiteeActivity.REPO_DATA_PAGE, null)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            content.hidden = true
            content.scrollTo(0, 0)
            contentLoading.hide()
        } else
            arguments?.apply {
                val repoOwner = getString(REPO_OWNER, "")
                repoName = getString(REPO_NAME, "")
                val repoSha = getString(REPO_SHA, "")
                if (repoOwner.isNotBlank() && repoName.isNotBlank() && repoSha.isNotBlank()) {
                    contentLoading.show()
                    viewModel.requestFileContent(repoOwner, repoName, repoSha)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentLoading.hide()
    }
}