package lishui.module.media.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : `api.apiopen.top`主机地址的 GET请求
 */
abstract class ApiOpenGetTask : HttpGetTask() {

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTPS

    override fun host(): String = "api.apiopen.top"
}