package lishui.module.flutter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import lishui.module.flutter.channel.FlutterAppChannel

/**
 *  author : linlishui
 *  time   : 2021/11/30
 *  desc   : Flutter 模块的宿主原生页面
 */
class FlutterHostActivity : FlutterActivity() {

    private lateinit var flutterAppChannel: FlutterAppChannel

    private val flutterTask = Runnable {
        // 原生端调用Flutter方法
        val map = mapOf(
            FlutterAppChannel.ARG_NATIVE_INVOKE to "native -> flutter: ${System.currentTimeMillis()}"
        )
        flutterAppChannel.channel.invokeMethod(FlutterAppChannel.METHOD_TEST_FLUTTER, map)
        postFlutterTask()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postFlutterTask()
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        flutterAppChannel = FlutterAppChannel(flutterEngine.dartExecutor)
    }

    private fun postFlutterTask() {
        window.decorView.postDelayed(flutterTask, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        window.decorView.removeCallbacks(flutterTask)
    }

    companion object {

        private fun createIntent(
            context: Context,
            cachedEngineId: String = "my_engine_id",
            transparentMode: Boolean = true,
            destroyEngineWithActivity: Boolean = false
        ): Intent {

            return Intent(context, FlutterHostActivity::class.java)
                .putExtra("cached_engine_id", cachedEngineId)
                .putExtra("destroy_engine_with_activity", destroyEngineWithActivity)
                .putExtra(
                    "destroy_engine_with_activity",
                    if (transparentMode)
                        FlutterActivityLaunchConfigs.BackgroundMode.transparent.name
                    else
                        FlutterActivityLaunchConfigs.BackgroundMode.opaque.name
                )
        }

        fun startFlutterHostPage(context: Context) {
            val intent = createIntent(context)
            context.startActivity(intent)
        }
    }
}