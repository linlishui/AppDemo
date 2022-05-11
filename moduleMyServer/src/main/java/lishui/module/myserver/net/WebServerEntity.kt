package lishui.module.myserver.net

/**
 *  author : linlishui
 *  time   : 2021/12/28
 *  desc   : 本地web-service网络实体类
 */

/* 网络请求实体类 */

data class WebResult<T>(
    val data: T,
    val code: Int,
    val message: String
)

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val extra: String
)