package lishui.module.myserver.net.task

import com.google.gson.JsonObject
import lishui.lib.base.log.LogUtils
import lishui.module.myserver.net.Greeting
import lishui.module.myserver.net.LocalServerGetTask
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/12/28
 *  desc   : greeting本地接口请求
 */
class GreetingTask : LocalServerGetTask() {

    override fun path(): String = "greeting"

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            val greeting = JsonUtil.fromJson(jsonObject, Greeting::class.java)
            LogUtils.d("GreetingTask unpackResult: ${greeting.toString()}")
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}