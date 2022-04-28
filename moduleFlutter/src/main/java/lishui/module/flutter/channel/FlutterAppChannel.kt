package lishui.module.flutter.channel

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import lishui.lib.base.log.LogUtils

/**
 *  author : linlishui
 *  time   : 2021/11/30
 *  desc   : Flutter与原生端双向通信channel
 */
class FlutterAppChannel(
    binaryMessenger: BinaryMessenger
) : MethodChannel.MethodCallHandler {

    val channel: MethodChannel = MethodChannel(binaryMessenger, CHANNEL_NAME)

    init {
        channel.setMethodCallHandler(this)
    }

    // Flutter调用原生端方法
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        LogUtils.d("[FlutterAppChannel] ${call.method} method invoked: flutter -> native")
        when (call.method) {
            METHOD_TEST_NATIVE -> result.success("flutter -> native: ${System.currentTimeMillis()}")
            else -> result.notImplemented()
        }
    }

    companion object {
        const val CHANNEL_NAME = "lishui.demo.flutter.channel"

        const val METHOD_TEST_NATIVE = "testNativeInvoke"
        const val METHOD_TEST_FLUTTER = "testFlutterInvoke"

        const val ARG_NATIVE_INVOKE = "native_arg"
    }
}