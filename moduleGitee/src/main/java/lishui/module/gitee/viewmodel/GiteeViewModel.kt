package lishui.module.gitee.viewmodel

import android.lib.base.log.LogUtils
import android.util.Base64
import android.util.LruCache
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import lishui.module.gitee.model.UserRepo
import lishui.module.gitee.net.GiteeToken
import lishui.module.gitee.net.RepoTree
import lishui.module.gitee.net.task.*
import lishui.module.gitee.ui.GiteeActivity
import lishui.service.core.SharedPrefs
import lishui.service.net.NetClient

/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */

class GiteeViewModel : ViewModel() {

    // 缓存repo tree数据
    private val repoDataCache = LruCache<String, List<RepoTree>>(30)

    // 缓存repo content数据
    private val repoContentCache = LruCache<String, String>(16)

    private var accessToken: String = ""

    private val userRepoMap = HashMap<Int, List<UserRepo>>()

    private val _pageChangedLiveData = MutableLiveData<String>()
    val pageChangedLiveData = _pageChangedLiveData as LiveData<String>

    private val _repoListLiveData = MutableLiveData<List<UserRepo>>()
    val repoListLiveData = _repoListLiveData as LiveData<List<UserRepo>>

    private val _repoTreeListLiveData = MutableLiveData<List<RepoTree>>()
    val repoTreeListLiveData = _repoTreeListLiveData as LiveData<List<RepoTree>>

    private val _blobContentLiveData = MutableLiveData<String>()
    val blobContentLiveData = _blobContentLiveData as LiveData<String>

    fun initGitee() {
        viewModelScope.launch(Dispatchers.IO) {

            accessToken = SharedPrefs.getString(ACCESS_TOKEN_KEY, "") ?: ""
            val refreshTokenTime = SharedPrefs.getString(REFRESH_TOKEN_TIME_KEY, "")

            if (refreshTokenTime.isNullOrBlank() || accessToken.isNullOrBlank()) {
                _pageChangedLiveData.postValue(GiteeActivity.LOGIN_PAGE)
            } else {
                val tokenTimeList = refreshTokenTime.split(TOKEN_TIME_SPLIT)
                if (tokenTimeList.size < 2) {
                    throw IllegalStateException("invalid toke time string")
                }
                val time = tokenTimeList[1].toLong()
                if (time < System.currentTimeMillis()) {
                    // invalid time for token, try to update it
                    val giteeTokenTask = NetClient.execute(GiteeRefreshTokenTask(tokenTimeList[0]))
                    updateGiteeToken(giteeTokenTask.giteeToken)
                }
                _pageChangedLiveData.postValue(GiteeActivity.REPO_LIST_PAGE)
            }
        }
    }

    fun loginGitee(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val giteeTokenTask = NetClient.execute(GiteeRequestTokenTask(userName, password))
            updateGiteeToken(giteeTokenTask.giteeToken)
            _pageChangedLiveData.postValue(GiteeActivity.REPO_LIST_PAGE)
        }
    }

    private fun updateGiteeToken(giteeToken: GiteeToken) {
        val expiredTime = giteeToken.expiresIn * 1000 + System.currentTimeMillis()
        accessToken = giteeToken.accessToken
        SharedPrefs.putString(ACCESS_TOKEN_KEY, accessToken)
        SharedPrefs.putString(
            REFRESH_TOKEN_TIME_KEY,
            "${giteeToken.refreshToken}$TOKEN_TIME_SPLIT$expiredTime"
        )
    }

    fun listGiteeRepoList(
        keyWord: String = "",
        page: Int = 1,
        token: String = accessToken
    ) {
        if (token.isBlank()) {
            throw IllegalStateException("access token is null or empty")
        }
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val reposTask = NetClient.execute(GiteeListReposTask(token, keyWord, page))
                val originalRepoList = reposTask.giteeUserRepoList
                val userRepoList = arrayListOf<UserRepo>()
                for (giteeUserRepo in originalRepoList) {
                    val userRepo = UserRepo(
                        id = giteeUserRepo.id,
                        path = giteeUserRepo.path,
                        repoName = giteeUserRepo.name,
                        ownerName = giteeUserRepo.owner?.login ?: "",
                        defaultBranch = giteeUserRepo.defaultBranch,
                        updateTime = giteeUserRepo.updateTime
                    )
                    userRepoList.add(userRepo)
                }
                userRepoMap[page] = userRepoList
                _repoListLiveData.postValue(userRepoList)
            } catch (ex: Exception) {
                _repoListLiveData.postValue(emptyList())
            }
        }
    }

    fun listGiteeRepoData(
        owner: String,
        repo: String,
        sha: String,
        token: String = accessToken
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val repoDataTask = NetClient.execute(GiteeRepoDataTask(token, owner, repo, sha))
                repoDataTask.repoData.tree?.apply {
                    repoDataCache.put(repo + "_" + sha, this)
                    _repoTreeListLiveData.postValue(this)
                } ?: _repoTreeListLiveData.postValue(emptyList())
            } catch (ex: Exception) {
                LogUtils.d("listGiteeRepoData error:$ex")
                _repoTreeListLiveData.postValue(emptyList())
            }
        }
    }

    fun getGiteeRepoDataFromCache(sha: String, failCallback: () -> Unit) {
        val repoTrees = repoDataCache.get(sha)
        if (repoTrees == null) {
            failCallback.invoke()
        } else {
            _repoTreeListLiveData.value = repoTrees
        }
    }

    fun requestFileContent(
        owner: String,
        repo: String,
        sha: String,
        token: String = accessToken
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val blobDataTask = NetClient.execute(GiteeRepoBlobTask(token, owner, repo, sha))
                val originalBlobData = blobDataTask.blobData
                // Base64解码
                val contentByteArray = Base64.decode(originalBlobData.content, Base64.DEFAULT)
                val contentData = String(bytes = contentByteArray)
                repoContentCache.put(sha, contentData)
                _blobContentLiveData.postValue(contentData)
            } catch (ex: Exception) {
                _blobContentLiveData.postValue("requestFileContent error:$ex")
            }
        }
    }

    @Deprecated("用于测试")
    private fun testViewModelScope() {
        LogUtils.d("1. thread name=${Thread.currentThread().name}, id=${Thread.currentThread().id}")
        viewModelScope.launch(CoroutineName("main-view-model")) {
            val deferred1 = async {
                withContext(Dispatchers.Default) {
                    try {
                        LogUtils.d("2. Dispatchers.Default name=${Thread.currentThread().name}, id=${Thread.currentThread().id}")
                        throw IllegalStateException("hi")
                    } catch (ex: Exception) {

                    }
                }
            }
            val deferred2 = async {
                withContext(Dispatchers.IO) {
                    LogUtils.d("3. Dispatchers.IO name=${Thread.currentThread().name}, id=${Thread.currentThread().id}")
                }
            }
            deferred1.await()
            deferred2.await()

            withContext(Dispatchers.Unconfined) {
                LogUtils.d("4. Dispatchers.Unconfined name=${Thread.currentThread().name}, id=${Thread.currentThread().id}")
            }
            withContext(Dispatchers.Main) {
                LogUtils.d("5. Dispatchers.Main name=${Thread.currentThread().name}, id=${Thread.currentThread().id}")
            }
        }

    }

    companion object {
        const val TOKEN_TIME_SPLIT = "#"
        const val ACCESS_TOKEN_KEY = "gitee_access_token"
        const val REFRESH_TOKEN_TIME_KEY = "gitee_refresh_token_time"
    }
}