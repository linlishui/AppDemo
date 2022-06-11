package lishui.module.myserver.net.task

import android.lib.base.log.LogUtils
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.myserver.net.MyServerNetConfigs.isRequestSuccess
import lishui.module.myserver.net.User
import lishui.module.myserver.net.WebResult
import lishui.module.myserver.net.WebServerGetTask
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/12/28
 *  desc   : say-hello 本地接口请求
 */
class SayHelloTask : WebServerGetTask() {

    //override fun url(): String = "http://192.168.31.146:8080/api/v2/test/user?content=world"

    override fun path(): String = "api/v2/test/user"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("content", "world")
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson<WebResult<User>>(
                jsonObject,
                object : TypeToken<WebResult<User>>() {}.type
            )?.apply {
                if (this.isRequestSuccess()) {
                    LogUtils.d("SayHelloTask request successfully with data=${this.data}")
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}