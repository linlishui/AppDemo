package lishui.module.wanandroid.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : 进行WanAndroid中http的GET请求任务
 */
abstract class WanGetTask : HttpGetTask() {

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTPS

    override fun host(): String = WanNetConfigs.HOST_WAN_ANDROID

}