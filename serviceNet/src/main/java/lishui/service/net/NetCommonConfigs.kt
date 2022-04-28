package lishui.service.net

import lishui.service.net.result.NetExceptionResult
import lishui.service.net.result.NetResult

// 网络配置常量
object NetCommonConfigs {

    const val NET_TAG = "NetCommonConfigs"
    const val NET_DEBUG = true

    // schema
    const val SCHEMA_HTTP = "http"
    const val SCHEMA_HTTPS = "https"

    // 网络相关的拓展函数
    fun NetResult.isSuccess() = this !is NetExceptionResult

}