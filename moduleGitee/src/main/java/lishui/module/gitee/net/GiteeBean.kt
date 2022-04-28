package lishui.module.gitee.net

import com.google.gson.annotations.SerializedName

/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */

data class GiteeToken(
    @SerializedName(value = "access_token") val accessToken: String = "",
    @SerializedName(value = "token_type") val tokenType: String = "",
    @SerializedName(value = "expires_in") val expiresIn: Int = 0,
    @SerializedName(value = "refresh_token") val refreshToken: String = "",
    @SerializedName(value = "created_at") val createdTime: Int = 0,
    val scope: String = ""
)

data class GiteeUserRepo(
    val id: Int = 0,
    val url: String = "",
    val path: String = "",
    val name: String = "",
    val relation: String = "",
    val private: Boolean = false,
    val public: Boolean = false,
    val internal: Boolean = false,
    val fork: Boolean = false,
    var owner: GiteeOwner? = null,
    @SerializedName(value = "full_name") val fullName: String = "",
    @SerializedName(value = "human_name") val humanName: String = "",
    @SerializedName(value = "html_url") val htmlUrl: String = "",
    @SerializedName(value = "ssh_url") val sshUrl: String = "",
    @SerializedName(value = "project_creator") val projectCreator: String = "",
    @SerializedName(value = "default_branch") val defaultBranch: String = "",
    @SerializedName(value = "updated_at") val updateTime: String = "",
)

data class GiteeOwner(
    val id: Int = 0,
    val login: String = "",
    val name: String = ""
)

data class RepoData(
    val sha: String = "",
    val url: String = "",
    val truncated: String = "",
    val tree: List<RepoTree>? = null
)

data class RepoTree(
    val path: String = "",
    val mode: String = "",
    val type: String = "",
    val sha: String = "",
    val size: Int = 0,
    val url: String = ""
)

data class BlobData(
    val size: Int = 0,
    val sha: String = "",
    val url: String = "",
    val content: String = "",
    val encoding: String = ""
)
