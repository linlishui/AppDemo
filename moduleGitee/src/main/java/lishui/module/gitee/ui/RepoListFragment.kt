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
import lishui.module.gitee.model.UserRepo
import lishui.module.gitee.ui.adapter.GiteeRepoListAdapter
import lishui.module.gitee.viewmodel.GiteeViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */

class RepoListFragment : GiteeFragment(), View.OnClickListener {

    private val repoListAdapter = GiteeRepoListAdapter()

    private lateinit var contentLoading: ContentLoadingProgressBar
    private lateinit var viewModel: GiteeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GiteeViewModel::class.java)
        viewModel.listGiteeRepoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gitee_repo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentLoading = view.findViewById(R.id.repo_loading)
        view.findViewById<RecyclerView>(R.id.repo_list).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = repoListAdapter.also { it.setClickListener(this@RepoListFragment) }
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.repoListLiveData.observe(requireActivity()) {
            repoListAdapter.updateItems(it)
            contentLoading.hide()
        }
    }

    override fun onClick(v: View?) {
        val userRepo = v?.tag
        if (userRepo is UserRepo) {
            activity?.run {
                if (this is GiteeActivity) {
                    val userRepoBundle = Bundle()
                    userRepoBundle.putString(REPO_NAME, userRepo.repoName)
                    userRepoBundle.putString(REPO_OWNER, userRepo.ownerName)
                    userRepoBundle.putString(REPO_SHA, userRepo.defaultBranch)
                    this.switchFragment(GiteeActivity.REPO_DATA_PAGE, userRepoBundle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentLoading.hide()
    }
}