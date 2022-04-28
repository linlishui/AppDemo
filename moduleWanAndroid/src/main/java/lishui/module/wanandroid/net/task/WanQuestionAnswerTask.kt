package lishui.module.wanandroid.net.task

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.wanandroid.net.PageBody
import lishui.module.wanandroid.net.QnAEntity
import lishui.module.wanandroid.net.WanGetTask
import lishui.module.wanandroid.net.WanNetConfigs.isWanRequestSuccess
import lishui.module.wanandroid.net.WanResult
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : WanAndroid的问答API请求任务
 */
class WanQuestionAnswerTask(private val pagePathSeg: String) : WanGetTask() {

    val wanQnAList = ArrayList<QnAEntity>()

    override fun path(): String = "wenda/list/$pagePathSeg/json"

    override fun unpackResult(jsonObject: JsonObject) {
        try {
            JsonUtil.fromJson<WanResult<PageBody<QnAEntity>>>(
                jsonObject,
                object : TypeToken<WanResult<PageBody<QnAEntity>>>() {}.type
            )?.apply {
                if (this.isWanRequestSuccess()) {
                    wanQnAList.addAll(this.data.datas)
                }
            }

        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }

}