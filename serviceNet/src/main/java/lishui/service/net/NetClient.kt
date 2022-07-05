package lishui.service.net

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lishui.service.net.NetCommonConfigs.NET_DEBUG
import lishui.service.net.NetCommonConfigs.isSuccess
import lishui.service.net.http.HttpBaseTask
import lishui.service.net.http.HttpDns
import lishui.service.net.http.HttpEventListener
import lishui.service.net.result.NetExceptionResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *  author : linlishui
 *  time   : 2021/10/15
 *  desc   : 网络客户端
 */
object NetClient {

    private val client: OkHttpClient by lazy {

        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        if (NET_DEBUG) {
            okhttpClientBuilder
                .dns(HttpDns())
                .eventListener(HttpEventListener())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        } else {
            okhttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        }

        okhttpClientBuilder.build()
    }

    fun core(): OkHttpClient = client

    @WorkerThread
    fun <T : HttpBaseTask> request(
        task: T,
        successBlock: T.() -> Unit = { },
        failBlock: (NetExceptionResult) -> Unit = { }
    ) {
        val netResult = task.execute()
        if (netResult.isSuccess()) {
            successBlock.invoke(task)
        } else {
            failBlock.invoke(netResult as NetExceptionResult)
        }
    }

    // todo: 异常或者成功状态，待处理
    suspend fun <T : HttpBaseTask> execute(httpTask: T): T = withContext(Dispatchers.IO) {
        val netResult = httpTask.execute()
        return@withContext httpTask
    }
}