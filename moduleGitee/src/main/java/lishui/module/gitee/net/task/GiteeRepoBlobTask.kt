package lishui.module.gitee.net.task

import com.google.gson.JsonObject
import lishui.module.gitee.net.BlobData
import lishui.module.gitee.net.GiteeGetTask
import lishui.module.gitee.net.RepoData
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : 获取文件数据
 */
class GiteeRepoBlobTask(
    private val token: String,
    private val owner: String,
    private val repo: String,
    private val sha: String
) : GiteeGetTask() {

    var blobData: BlobData = BlobData()

    override fun path(): String = "api/v5/repos/${owner}/${repo}/git/blobs/${sha}"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("access_token", token)
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson(jsonObject, BlobData::class.java)?.apply {
                blobData = this
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}