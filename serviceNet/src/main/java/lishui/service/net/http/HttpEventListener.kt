package lishui.service.net.http

import android.lib.base.log.LogUtils
import lishui.service.net.NetCommonConfigs.NET_TAG
import okhttp3.Call
import okhttp3.EventListener
import java.io.IOException

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   : 网络请求过程中各事件流程监听
 */
class HttpEventListener : EventListener() {

    override fun callStart(call: Call) {
        super.callStart(call)
        LogUtils.d(NET_TAG, ">>>>>>>>>> HttpEventListener on callStart <<<<<<<<<<")
        LogUtils.d(NET_TAG, call.request().toString())
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        LogUtils.d(NET_TAG, ">>>>>>>>>> HttpEventListener on callEnd <<<<<<<<<<")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        LogUtils.e(NET_TAG, "HttpEventListener callFailed!", ioe)
        LogUtils.d(NET_TAG, ">>>>>>>>>> HttpEventListener on callFailed <<<<<<<<<<")
    }
}