package lishui.module.wanandroid.net.task

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.wanandroid.net.NavData
import lishui.module.wanandroid.net.WanGetTask
import lishui.module.wanandroid.net.WanNetConfigs.isWanRequestSuccess
import lishui.module.wanandroid.net.WanResult
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/11/16
 *  desc   : WanAndroid的导航数据 请求API
 */
class WanNavListTask : WanGetTask() {

    var wanNavList: ArrayList<NavData> = arrayListOf()

    override fun path(): String = "navi/json"

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson<WanResult<List<NavData>>>(
                jsonObject,
                object : TypeToken<WanResult<List<NavData>>>() {}.type
            )?.apply {
                if (this.isWanRequestSuccess()) {
                    wanNavList.clear()
                    wanNavList.addAll(this.data)
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }

}