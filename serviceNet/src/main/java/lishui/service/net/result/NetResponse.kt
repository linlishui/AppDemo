package lishui.service.net.result

import okhttp3.Response
import okhttp3.ResponseBody

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : 网络返回封装
 */
class NetResponse(
    val code: Int,
    val message: String,
    val body: ResponseBody?,
    val rawResponse: Response?
) {

    fun toShortString(): String {
        return "NetResponse Brief status {code=$code, message=$message}"
    }
}