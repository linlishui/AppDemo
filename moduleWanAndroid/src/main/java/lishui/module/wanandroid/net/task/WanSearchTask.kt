package lishui.module.wanandroid.net.task

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.wanandroid.net.Article
import lishui.module.wanandroid.net.PageBody
import lishui.module.wanandroid.net.WanNetConfigs.isWanRequestSuccess
import lishui.module.wanandroid.net.WanPostTask
import lishui.module.wanandroid.net.WanResult
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : WanAndroid的搜索API请求任务
 */
class WanSearchTask(
    private val pagePathSeg: String,
    private val keyword: String = ""
) : WanPostTask() {

    var pageBody: PageBody<Article> = PageBody()
    //val articlesList = ArrayList<Article>()

    override fun path(): String = "article/query/$pagePathSeg/json"

    override fun buildFormBodyArgs(): HashMap<String, String> = hashMapOf("k" to keyword)

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson<WanResult<PageBody<Article>>>(
                jsonObject,
                object : TypeToken<WanResult<PageBody<Article>>>() {}.type
            )?.apply {
                if (this.isWanRequestSuccess()) {
                    pageBody = this.data
                    //articlesList.addAll(pageBody.datas)
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}