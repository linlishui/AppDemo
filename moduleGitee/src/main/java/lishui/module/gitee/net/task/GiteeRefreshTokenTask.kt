package lishui.module.gitee.net.task

import com.google.gson.JsonObject
import lishui.module.gitee.net.GiteePostTask
import lishui.module.gitee.net.GiteeToken
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : 更新Gitee的token
 */
class GiteeRefreshTokenTask(
    private val refreshToken: String
) : GiteePostTask() {

    var giteeToken: GiteeToken = GiteeToken()

    override fun path(): String = "oauth/token"

    override fun buildFormBodyArgs(): HashMap<String, String> = hashMapOf(
        "refresh_token" to refreshToken,
        "grant_type" to "refresh_token",
    )

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson(jsonObject, GiteeToken::class.java)?.apply {
                giteeToken = this
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}