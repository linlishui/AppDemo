package lishui.module.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lishui.android.ui.widget.list.RecyclerData
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.net.NavData
import lishui.module.wanandroid.net.TreeData
import lishui.module.wanandroid.net.task.WanNavListTask
import lishui.module.wanandroid.net.task.WanTreeListTask
import lishui.module.wanandroid.source.NavPageDataSource
import lishui.module.wanandroid.source.SearchPageDataSource
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem
import lishui.module.wanandroid.ui.util.WanNavTreeViewType
import lishui.service.net.NetClient

/**
 * Created by lishui.lin on 20-11-12
 */
class WanAndroidViewModel : ViewModel() {

    private val _treeDataLiveData = MutableLiveData<List<RecyclerData>>()
    val treeDataLiveData = _treeDataLiveData as LiveData<List<RecyclerData>>

    private val _navDataLiveData = MutableLiveData<List<RecyclerData>>()
    val navDataLiveData = _navDataLiveData as LiveData<List<RecyclerData>>

    fun loadNavTreeData(isNavType: Boolean = true) {
        viewModelScope.launch {
            if (isNavType) {
                loadNavData()
            } else {
                loadTreeData()
            }
        }
    }

    private suspend fun loadTreeData() = withContext(Dispatchers.Default) {
        try {
            val response: List<TreeData> = NetClient.execute(WanTreeListTask()).wanTreeList
            var parentId = -1
            val recyclerDataList = arrayListOf<RecyclerData>()
            response.forEach {
                if (parentId != it.id) {
                    parentId = it.id
                    val wanNavTreeItem = WanNavTreeItem(it.id, it.name)
                    recyclerDataList.add(RecyclerData(wanNavTreeItem, WanNavTreeViewType.VIEW_TYPE_PARENT_NAV_TREE))
                }

                it.children?.forEach { subTreeData ->
                    val wanNavTreeItem = WanNavTreeItem(subTreeData.id, subTreeData.name)
                    recyclerDataList.add(RecyclerData(wanNavTreeItem, WanNavTreeViewType.VIEW_TYPE_CHILDREN_NAV_TREE))
                }
            }
            _treeDataLiveData.postValue(recyclerDataList)
        } catch (e: Exception) {
            // no-op
        }
    }

    private suspend fun loadNavData() = withContext(Dispatchers.Default) {
        try {
            val response: List<NavData> = NetClient.execute(WanNavListTask()).wanNavList
            val recyclerDataList = arrayListOf<RecyclerData>()
            response.forEach {
                val parentNavTreeItem = WanNavTreeItem(it.cid, it.name)
                recyclerDataList.add(RecyclerData(parentNavTreeItem, WanNavTreeViewType.VIEW_TYPE_PARENT_NAV_TREE))
                it.articles?.forEach { article ->
                    val childNavTreeItem = WanNavTreeItem(article.id, article.title, article.link)
                    recyclerDataList.add(RecyclerData(childNavTreeItem, WanNavTreeViewType.VIEW_TYPE_CHILDREN_NAV_TREE))
                }
            }
            _navDataLiveData.postValue(recyclerDataList)
        } catch (e: Exception) {
            // no-op
        }
    }

    fun listArticlesByTreeId(id: Int): Flow<PagingData<Article>> = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        )
    ) {
        NavPageDataSource().apply {
            this.id = id
        }
    }.flow.cachedIn(viewModelScope)

    fun searchKeyword(key: String): Flow<PagingData<Article>> = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        )
    ) {
        SearchPageDataSource(keyword = key)
    }.flow.cachedIn(viewModelScope)

}