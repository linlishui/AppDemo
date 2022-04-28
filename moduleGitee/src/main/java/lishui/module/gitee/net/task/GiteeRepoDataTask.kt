package lishui.module.gitee.net.task

import com.google.gson.JsonObject
import lishui.module.gitee.net.GiteeGetTask
import lishui.module.gitee.net.RepoData
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : 获取仓库目录
 */
class GiteeRepoDataTask(
    private val token: String,
    private val owner: String,
    private val repo: String,
    private val sha: String
) : GiteeGetTask() {

    var repoData: RepoData = RepoData()

    override fun path(): String = "api/v5/repos/${owner}/${repo}/git/trees/${sha}"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("access_token", token)
        addQueryParam("recursive", "0")
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson(jsonObject, RepoData::class.java)?.apply {
                repoData = this
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}