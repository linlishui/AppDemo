package lishui.module.wanandroid.net.task

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.net.PageBody
import lishui.module.wanandroid.net.WanGetTask
import lishui.module.wanandroid.net.WanNetConfigs.isWanRequestSuccess
import lishui.module.wanandroid.net.WanResult
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/11/16
 *  desc   : WanAndroid的导航下的文章列表 请求API
 */
class WanNavArticlesTask(
    private val categoryId: Int,
    private val pagePathSeg: String,
) : WanGetTask() {

    var pageBody: PageBody<Article> = PageBody()

    override fun path(): String = "article/list/${pagePathSeg}/json"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("cid", categoryId.toString())
    }

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson<WanResult<PageBody<Article>>>(
                jsonObject,
                object : TypeToken<WanResult<PageBody<Article>>>() {}.type
            )?.apply {
                if (this.isWanRequestSuccess()) {
                    pageBody = this.data
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }

}