package lishui.service.net.http

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lishui.lib.base.log.LogUtils
import lishui.service.net.NetClient
import lishui.service.net.result.*
import okhttp3.*
import java.io.Reader
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *  author : linlishui
 *  time   : 2021/11/11
 *  desc   : 公共Http任务
 */
abstract class HttpBaseTask {

    private val httpUrlBuilder: HttpUrl.Builder = HttpUrl.Builder()

    private val headersBuilder: Headers.Builder = Headers.Builder()

    private val queryParams: HashMap<String, String> = HashMap()

    abstract fun schema(): String

    abstract fun host(): String

    abstract fun path(): String

    internal abstract fun makeRequest(): Request

    // 原始url，请求优先级最高。默认为空值
    open fun url(): String = ""

    // 端口设置。默认为-1
    open fun port(): Int = -1

    /* http url一般格式： {schema://host:port/path?query} */
    internal fun makeHttpUrl(): HttpUrl = httpUrlBuilder
        .scheme(schema())
        .host(host()).apply {

            // add port if need
            val port = port()
            if (port != -1) port(port())

            // add path if need
            val path = path()
            if (path.isNotBlank()) addPathSegment(path)

        }.apply {
            // add query if need
            makeQueryParams()
            queryParams.forEach {
                addQueryParameter(it.key, it.value)
            }

        }.build()

    internal fun makeBody(): RequestBody? {

        val formBodyArgs = buildFormBodyArgs()
        if (formBodyArgs != null && formBodyArgs.size > 0) {
            val formBodyBuilder = FormBody.Builder()
            formBodyArgs.forEach { (name, value) ->
                formBodyBuilder.add(name, value)
            }
            return formBodyBuilder.build()
        }
        return null
    }

    internal fun makeHeaders(): Headers {
        val buildHeaders = buildHeaders()
        if (buildHeaders != null && buildHeaders.size > 0) {
            buildHeaders.forEach { (key, value) ->
                headersBuilder[key] = value
            }
        }
        return headersBuilder.build()
    }

    open fun makeQueryParams() {}

    open fun buildHeaders(): HashMap<String, String>? = null

    open fun buildFormBodyArgs(): HashMap<String, String>? = null

    fun addQueryParam(key: String, value: String) {
        queryParams[key] = value
    }

    fun execute(): NetResult {
        try {
            val response: Response = http()
            val reader = parseResponse(
                NetResponse(
                    code = response.code,
                    message = response.message,
                    body = response.body,
                    rawResponse = response
                )
            )
            val jsonObject = json(reader)
            unpackResult(jsonObject)
            response.close()
            return NetJsonObjectResult(jsonObject)
        } catch (ex: NetException) {
            LogUtils.e(this.javaClass.simpleName, "occur exception when execute network task", ex)
            return NetExceptionResult(ex)
        }
    }

    @Throws(NetException::class)
    open fun unpackResult(jsonObject: JsonObject) {
    }

    @Throws(NetException::class)
    private fun http(): Response {
        try {
            return NetClient.core()
                .newCall(makeRequest())
                .execute()
        } catch (tr: Throwable) {
            when (tr) {
                is UnknownHostException -> {
                    throw NetworkError(tr)
                }
                is SocketTimeoutException -> {
                    throw NetTimeout(tr)
                }
                else -> {
                    throw NetworkError(tr)
                }
            }
        }
    }

    @Throws(NetException::class)
    private fun parseResponse(netResponse: NetResponse): Reader {

        LogUtils.d(this.javaClass.simpleName, netResponse.toShortString())

        if (netResponse.code != 200) {
            throw NetNotOk(netResponse.code)
        }

        if (netResponse.body == null) {
            throw NetUnspecified("parse response fail with empty body")
        }

        try {
            return netResponse.body.charStream()
        } catch (ex: Exception) {
            throw NetUnspecified("parse response fail because get reader error")
        }
    }

    @Throws(NetException::class)
    private fun json(reader: Reader): JsonObject {
        val element: JsonElement = try {
            JsonParser.parseReader(reader)
        } catch (tr: Throwable) {
            throw NetJsonError(tr)
        }

        return if (element.isJsonObject) {
            element.asJsonObject
        } else {
            throw NetJsonError(IllegalStateException("Cannot be converted into json object"))
        }
    }
}