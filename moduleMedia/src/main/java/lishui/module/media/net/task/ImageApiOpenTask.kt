package lishui.module.media.net.task

import androidx.annotation.IntRange
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import lishui.module.media.net.ApiOpenGetTask
import lishui.module.media.net.ApiOpenResult
import lishui.module.media.net.ImageApiData
import lishui.module.media.net.PageBody
import lishui.service.net.result.NetJsonError
import lishui.service.net.util.JsonUtil

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 网络图片请求
 */
class ImageApiOpenTask(@IntRange(from = 0) val page: Int, @IntRange(from = 1, to = 20) val size: Int = 20) : ApiOpenGetTask() {

    var imageList = ArrayList<ImageApiData>()
        private set

    override fun path(): String = "api/getImages"

    override fun makeQueryParams() {
        super.makeQueryParams()
        addQueryParam("page", page.toString())
        addQueryParam("size", size.toString())
    }

    override fun unpackResult(jsonObject: JsonObject) {
        super.unpackResult(jsonObject)
        try {
            JsonUtil.fromJson<ApiOpenResult<PageBody<ImageApiData>>>(
                jsonObject,
                object : TypeToken<ApiOpenResult<PageBody<ImageApiData>>>() {}.type
            )?.apply {
                if (this.code == 200) {
                    imageList.addAll(this.result.list)
                }
            }
        } catch (ex: Exception) {
            throw NetJsonError(ex)
        }
    }
}