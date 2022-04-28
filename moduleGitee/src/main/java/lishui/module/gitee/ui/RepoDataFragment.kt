package lishui.module.gitee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lishui.module.gitee.R
import lishui.module.gitee.net.RepoTree
import lishui.module.gitee.ui.adapter.GiteeRepoDataAdapter
import lishui.module.gitee.ui.holder.GITEE_BLOB_TYPE
import lishui.module.gitee.viewmodel.GiteeViewModel
import java.util.*

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
const val REPO_OWNER = "repo_owner"
const val REPO_NAME = "repo_name"
const val REPO_SHA = "repo_sha"

class RepoDataFragment : GiteeFragment(), View.OnClickListener {

    private val repoDataAdapter = GiteeRepoDataAdapter()

    private lateinit var viewModel: GiteeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var contentLoading: ContentLoadingProgressBar

    private var mRepoOwner: String = ""
    private var mRepoName: String = ""
    private var mRepoSha: String = ""

    private var isLoading = false
    private val mNameShaStack = Stack<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GiteeViewModel::class.java)

        requireArguments().apply {
            mRepoOwner = getString(REPO_OWNER, "")
            mRepoSha = getString(REPO_SHA, "")
            mRepoName = getString(REPO_NAME, "")
            if (mRepoOwner.isNotBlank() && mRepoName.isNotBlank() && mRepoSha.isNotBlank()) {
                val cacheKey = getCacheKey(mRepoName, mRepoSha)
                mNameShaStack.push(cacheKey)
                viewModel.getGiteeRepoDataFromCache(cacheKey) {
                    viewModel.listGiteeRepoData(mRepoOwner, mRepoName, mRepoSha)
                }
            } else {
                throw IllegalStateException("RepoDataFragment can not find valid args in onCreate")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gitee_repo_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentLoading = view.findViewById(R.id.repo_data_loading)
        recyclerView = view.findViewById<RecyclerView>(R.id.repo_data_list)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoDataAdapter.also { it.setClickListener(this@RepoDataFragment) }
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.repoTreeListLiveData.observe(requireActivity()) {
            isLoading = false
            contentLoading.hide()
            repoDataAdapter.updateItems(it)
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        if (isLoading) {
            return
        }
        val repoTree = v?.tag
        if (repoTree is RepoTree) {
            if (repoTree.type == GITEE_BLOB_TYPE) {
                activity?.run {
                    if (this is GiteeActivity) {
                        val blobDataBundle = Bundle()
                        blobDataBundle.putString(REPO_NAME, mRepoName)
                        blobDataBundle.putString(REPO_OWNER, mRepoOwner)
                        blobDataBundle.putString(REPO_SHA, repoTree.sha)
                        this.switchFragment(GiteeActivity.BLOB_DATA_PAGE, blobDataBundle)
                    }
                }
            } else {
                val cacheKey = getCacheKey(mRepoName, repoTree.sha)
                mNameShaStack.push(cacheKey)
                viewModel.getGiteeRepoDataFromCache(cacheKey) {
                    isLoading = true
                    contentLoading.show()
                    viewModel.listGiteeRepoData(
                        owner = mRepoOwner,
                        repo = mRepoName,
                        sha = repoTree.sha
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isLoading) {
            return
        }

        if (mNameShaStack.size > 1) {
            mNameShaStack.pop()
            viewModel.getGiteeRepoDataFromCache(mNameShaStack.peek()) {
                val keyContent = getContentByCacheKey(mNameShaStack.peek())
                isLoading = true
                contentLoading.show()
                viewModel.listGiteeRepoData(
                    owner = mRepoOwner,
                    repo = keyContent[0],
                    sha = keyContent[1]
                )
            }
        } else {
            mNameShaStack.removeAllElements()
            activity?.run {
                if (this is GiteeActivity) {
                    this.switchFragment(GiteeActivity.REPO_LIST_PAGE, null)
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            contentLoading.hide()
            recyclerView.visibility = View.INVISIBLE
        } else {
            val bundle = arguments
            if (bundle != null) {
                val repoOwner = bundle.getString(REPO_OWNER, "")
                val repoName = bundle.getString(REPO_NAME, "")
                val repoSha = bundle.getString(REPO_SHA, "")
                if (repoOwner.isNotBlank() && repoName.isNotBlank() && repoSha.isNotBlank()) {
                    val cacheKey = getCacheKey(repoName, repoSha)
                    mNameShaStack.push(cacheKey)
                    mRepoSha = repoSha
                    mRepoName = repoName
                    mRepoOwner = repoOwner
                    viewModel.getGiteeRepoDataFromCache(cacheKey) {
                        contentLoading.show()
                        viewModel.listGiteeRepoData(repoOwner, repoName, repoSha)
                    }
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentLoading.hide()
    }

    private fun getCacheKey(name: String, sha: String): String = name + "_" + sha

    private fun getContentByCacheKey(cacheKey: String): List<String> = cacheKey.split("_")

}