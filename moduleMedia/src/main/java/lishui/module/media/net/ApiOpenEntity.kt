package lishui.module.media.net

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 拉取`api.apiopen.top`主机上的网络资源
 *           csdn 博客：https://blog.csdn.net/c__chao/article/details/78573737
 */

data class ApiOpenResult<T>(
    val result: T,
    val code: Int,
    val message: String
)

// 页面结果列表
data class PageBody<T>(
    val list: List<T> = emptyList(),
    val total: Int = 0
)

// 图片数据实体
data class ImageApiData(
    val id: Int,
    val title: String,
    val url: String,
    val type: String
)

// 视频数据实体
data class VideoApiData(
    val id: Int,
    val title: String,
    val userName: String,
    val userPic: String,
    val coverUrl: String,
    val playUrl: String,
    val duration: String
)