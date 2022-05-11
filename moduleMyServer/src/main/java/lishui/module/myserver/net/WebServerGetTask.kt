package lishui.module.myserver.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask

/**
 *  author : linlishui
 *  time   : 2021/12/28
 *  desc   : 本地web-service接口的GET请求
 */
abstract class WebServerGetTask : HttpGetTask() {

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTP

    // 10.255.215.35
    // 192.168.31.146
    override fun host(): String = "192.168.31.146"

    override fun port(): Int = 8080
}