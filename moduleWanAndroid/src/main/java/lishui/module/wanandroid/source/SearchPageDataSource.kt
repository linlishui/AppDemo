package lishui.module.wanandroid.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import lishui.lib.base.log.LogUtils
import lishui.service.net.NetClient
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.net.task.WanSearchTask

/**
 *  author : linlishui
 *  time   : 2021/4/23
 *  desc   : WanAndroid中Paging3的搜索数据源
 */
class SearchPageDataSource(private val keyword: String = "") : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            // Start refresh at page 0 if undefined.
            val page = params.key ?: 0
            val wanSearchTask = NetClient.execute(WanSearchTask(page.toString(), keyword))

            val count = wanSearchTask.pageBody.curPage * wanSearchTask.pageBody.size
            LogUtils.d("SearchPageDataSource page=$page, count=$count, total=${wanSearchTask.pageBody.total}")

            LoadResult.Page(
                data = wanSearchTask.pageBody.datas,
                prevKey = if (page > 0) page.minus(1) else null,
                nextKey = if (count >= wanSearchTask.pageBody.total) null else page.plus(1)
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}