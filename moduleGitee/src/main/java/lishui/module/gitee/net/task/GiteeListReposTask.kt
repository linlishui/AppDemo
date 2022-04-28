package lishui.module.gitee.net.task

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.gitee.net.GiteeGetTask
import lishui.module.gitee.net.GiteeUserRepo
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : 获取仓库列表
 */
class GiteeListReposTask(
    private val token: String,
    private val keyword: String = "",
    private val page: Int = 1
) : GiteeGetTask() {

    var giteeUserRepoList: List<GiteeUserRepo> = arrayListOf()

    override fun path(): String = "api/v5/user/repos"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("access_token", token)
        addQueryParam("q", keyword)
        addQueryParam("page", page.toString())
        addQueryParam("type", "all")
        addQueryParam("sort", "updated")
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson<List<GiteeUserRepo>>(
                jsonObject,
                object : TypeToken<List<GiteeUserRepo>>() {}.type
            )?.apply {
                giteeUserRepoList = this
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}