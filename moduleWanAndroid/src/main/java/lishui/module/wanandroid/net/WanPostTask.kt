package lishui.module.wanandroid.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpPostTask

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : 进行WanAndroid中http的POST请求任务
 */
abstract class WanPostTask : HttpPostTask() {

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTPS

    override fun host(): String = WanNetConfigs.HOST_WAN_ANDROID
}