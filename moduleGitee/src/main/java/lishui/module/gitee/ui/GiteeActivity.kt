package lishui.module.gitee.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lishui.lib.base.log.LogUtils
import lishui.module.gitee.viewmodel.GiteeViewModel

private const val TAG = "GiteeActivity"

class GiteeActivity : AppCompatActivity() {

    private lateinit var viewModel: GiteeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(GiteeViewModel::class.java)

        viewModel.initGitee()
        viewModel.pageChangedLiveData.observe(this) {
            switchFragment(it)
        }
    }

    internal fun switchFragment(tagName: String, initBundle: Bundle? = null) {
        val fragmentName = when (tagName) {
            LOGIN_PAGE -> LoginFragment::class.java.canonicalName
            REPO_LIST_PAGE -> RepoListFragment::class.java.canonicalName
            REPO_DATA_PAGE -> RepoDataFragment::class.java.canonicalName
            BLOB_DATA_PAGE -> BlobDataFragment::class.java.canonicalName
            else -> throw IllegalAccessException("can not find fragment using this name")
        }

        LogUtils.d(TAG, "switchFragment tag=$tagName, fragmentName=$fragmentName")
        val transaction = supportFragmentManager.beginTransaction()
        var targetFragment: Fragment? = supportFragmentManager.findFragmentByTag(tagName)
        if (targetFragment == null) {
            try {
                val clazz = Class.forName(fragmentName)
                targetFragment = clazz.getConstructor().newInstance() as Fragment
                targetFragment.arguments = initBundle
                transaction.add(Window.ID_ANDROID_CONTENT, targetFragment, tagName)
            } catch (ex: Exception) {
                LogUtils.e(TAG, "can not instance fragment: $tagName")
            }
        }

        supportFragmentManager.fragments.forEach {
            if (tagName == it.tag) {
                it.arguments = initBundle
                transaction.show(it)
            } else {
                transaction.hide(it)
            }
        }
        transaction.commit()
    }

    companion object {
        const val LOGIN_PAGE = "login_page"
        const val REPO_LIST_PAGE = "repo_list_page"
        const val REPO_DATA_PAGE = "repo_data_page"
        const val BLOB_DATA_PAGE = "blob_data_page"
    }
}