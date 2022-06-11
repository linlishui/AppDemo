package lishui.service.misc

import android.app.Application
import android.lib.base.component.StartupComponent
import android.os.Build
import android.os.Looper
import lishui.service.misc.performance.CrashHandler
import lishui.service.misc.performance.LooperPrinter

/**
 *  author : linlishui
 *  time   : 2021/11/18
 *  desc   : 未分类项的初始化，包括Looper log、crash检测等
 */
class MiscInitializer : StartupComponent {

    override fun init(app: Application) {
        super.init(app)

        CrashHandler.getInstance().init(app)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Looper.getMainLooper().setMessageLogging(LooperPrinter(Application.getProcessName()))
        }
    }
}