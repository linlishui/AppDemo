package lishui.module.media.net.task

import androidx.annotation.IntRange
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.media.net.*
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 网络视频请求
 */
class VideoApiOpenTask(@IntRange(from = 0) val page: Int, @IntRange(from = 1, to = 20) val size: Int = 20) : ApiOpenGetTask() {

    var videoList = ArrayList<VideoApiData>()
        private set

    override fun path(): String = "api/getHaoKanVideo"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("page", page.toString())
        addQueryParam("size", size.toString())
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson<ApiOpenResult<PageBody<VideoApiData>>>(
                jsonObject,
                object : TypeToken<ApiOpenResult<PageBody<VideoApiData>>>() {}.type
            )?.apply {
                if (this.code == 200) {
                    videoList.addAll(this.result.list)
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}