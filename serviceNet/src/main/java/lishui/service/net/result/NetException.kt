package lishui.service.net.result

/**
 *  author : linlishui
 *  time   : 2021/11/12
 *  desc   : 网络请求返回后出现的常见错误
 */

sealed class NetException : Exception()
data class NetUnspecified(val reason: String) : NetException()             // 未指定异常
data class NetworkError(val throwable: Throwable) : NetException()         // 网络错误
data class NetTimeout(val throwable: Throwable) : NetException()           // 从网络异常分开来, 超时提示系统繁忙
data class NetNotOk(val code: Int) : NetException()                        // http请求错误
data class NetJsonError(val throwable: Throwable) : NetException()         // json解析错误
