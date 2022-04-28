package lishui.service.web

import android.app.Application
import lishui.lib.base.component.StartupComponent

/**
 *  author : linlishui
 *  time   : 2021/11/16
 *  desc   : start-up Web端初始化任务
 */
class WebInitializer : lishui.lib.base.component.StartupComponent {

    override fun init(app: Application) {
        super.init(app)

        // 在调用TBS初始化、创建WebView之前进行如下配置
//        val settingsMap: HashMap<String, Any> = HashMap()
//        settingsMap[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        settingsMap[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(settingsMap)
    }
}