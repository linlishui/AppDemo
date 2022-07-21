package lishui.service.net

import lishui.service.net.result.NetJsonObjectResult
import lishui.service.net.result.NetResult

// 网络配置常量
object NetCommonConfigs {

    const val NET_TAG = "NetCommonConfigs"
    const val NET_DEBUG = false

    // schema
    const val SCHEMA_HTTP = "http"
    const val SCHEMA_HTTPS = "https"

    // 网络相关的拓展函数
    fun NetResult.isSuccess() = this is NetJsonObjectResult

}