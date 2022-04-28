package lishui.service.net.result

import com.google.gson.JsonObject

/**
 *  author : linlishui
 *  time   : 2021/11/12
 *  desc   : 网络结果返回，成功返回json对象，否则返回异常
 */
sealed class NetResult
data class NetJsonObjectResult(val jsonObject: JsonObject) : NetResult()
data class NetExceptionResult(val netException: NetException) : NetResult()