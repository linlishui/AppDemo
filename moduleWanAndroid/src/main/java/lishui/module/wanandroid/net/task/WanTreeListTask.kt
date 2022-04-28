package lishui.module.wanandroid.net.task

import android.net.Network
import android.net.NetworkSpecifier
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.wanandroid.net.TreeData
import lishui.module.wanandroid.net.WanGetTask
import lishui.module.wanandroid.net.WanNetConfigs.isWanRequestSuccess
import lishui.module.wanandroid.net.WanResult
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil
import java.io.File

/**
 *  author : linlishui
 *  time   : 2021/11/16
 *  desc   : WanAndroid的知识体系 请求API
 */
class WanTreeListTask : WanGetTask() {

    var wanTreeList: ArrayList<TreeData> = arrayListOf()

    override fun path(): String = "tree/json"

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson<WanResult<List<TreeData>>>(
                jsonObject,
                object : TypeToken<WanResult<List<TreeData>>>() {}.type
            )?.apply {
                if (this.isWanRequestSuccess()) {
                    wanTreeList.clear()
                    wanTreeList.addAll(this.data)
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }

}