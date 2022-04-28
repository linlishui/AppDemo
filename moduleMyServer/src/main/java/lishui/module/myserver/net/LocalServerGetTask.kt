package lishui.module.myserver.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask

/**
 *  author : linlishui
 *  time   : 2021/12/28
 *  desc   : 本地web-service接口的GET请求
 */
abstract class LocalServerGetTask : HttpGetTask() {

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTP

    override fun host(): String = "10.255.215.35"

    override fun port(): Int = 8080
}