package lishui.module.gitee.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : Gitee的GET请求任务
 */
abstract class GiteeGetTask : HttpGetTask(){

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTPS

    override fun host(): String = GiteeNetConfigs.GITEE_HOST
}