package lishui.module.flutter

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import lishui.lib.base.component.StartupComponent
import lishui.lib.base.log.LogUtils

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : Flutter 模块初始化
 */
class FlutterInitializer : lishui.lib.base.component.StartupComponent {

    override fun init(app: Application) {
        super.init(app)
        try {
            // Instantiate a FlutterEngine.
            val flutterEngine: FlutterEngine = FlutterEngine(app)

            // Start executing Dart code to pre-warm the FlutterEngine.
            flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            // Cache the FlutterEngine to be used by FlutterActivity.
            FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine)
        } catch (ex: Exception) {
            LogUtils.e("FlutterInitializer", "init occur error", ex)
        }
    }
}