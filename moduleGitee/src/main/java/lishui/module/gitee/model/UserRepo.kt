package lishui.module.gitee.model

/**
 * @author lishui.lin
 * Created it on 2021/5/28
 */
data class UserRepo(
    val id: Int = 0,
    val path: String = "",
    val repoName: String = "",
    val ownerName: String = "",
    val defaultBranch: String = "",
    val updateTime: String
)