package lishui.service.net.http

import okhttp3.Request

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : Http进行Post方法的请求任务
 */
abstract class HttpPostTask : HttpBaseTask() {

    final override fun makeRequest(): Request {
        val requestBuilder = if (url().isBlank()) {
            Request.Builder().url(makeHttpUrl())
        } else {
            Request.Builder().url(url())
        }

        return requestBuilder.apply {
            makeBody()?.let {
                this.post(it)
            } ?: throw IllegalStateException("HttpPostTask should have not null RequestBody!")
        }.headers(makeHeaders()).build()
    }
}