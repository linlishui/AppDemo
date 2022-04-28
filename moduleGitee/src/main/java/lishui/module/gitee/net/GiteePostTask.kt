package lishui.module.gitee.net

import lishui.service.net.NetCommonConfigs
import lishui.service.net.http.HttpGetTask
import lishui.service.net.http.HttpPostTask

/**
 *  author : linlishui
 *  time   : 2022/01/05
 *  desc   : Gitee的POST请求任务
 */
abstract class GiteePostTask : HttpPostTask(){

    override fun schema(): String = NetCommonConfigs.SCHEMA_HTTPS

    override fun host(): String = GiteeNetConfigs.GITEE_HOST
}