package lishui.module.gitee.net.task

import com.google.gson.JsonObject
import lishui.module.gitee.net.GiteeNetConfigs
import lishui.module.gitee.net.GiteePostTask
import lishui.module.gitee.net.GiteeToken
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : 请求Gitee的token
 */
class GiteeRequestTokenTask(
    private val userName: String,
    private val password: String
) : GiteePostTask() {

    var giteeToken: GiteeToken = GiteeToken()

    override fun path(): String = "oauth/token"

    override fun buildFormBodyArgs(): HashMap<String, String> = hashMapOf(
        "userName" to userName,
        "password" to password,
        "grant_type" to "password",
        "scope" to "projects user_info gists",
        "client_id" to GiteeNetConfigs.GITEE_CLIENT_ID,
        "client_secret" to GiteeNetConfigs.GITEE_CLIENT_SECRET
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