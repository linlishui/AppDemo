package lishui.module.flutter.ui

import android.app.Activity
import android.os.Bundle

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : Flutter 模块的入口页面
 */
class FlutterEntryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlutterHostActivity.startFlutterHostPage(this)
        finish()
    }
}