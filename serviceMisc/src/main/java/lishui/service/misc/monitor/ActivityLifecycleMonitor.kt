package lishui.service.misc.monitor

import android.app.Activity
import android.app.Application
import android.os.Bundle
import lishui.lib.base.log.LogUtils
import org.jetbrains.annotations.TestOnly

/**
 * @author lishui.lin
 * Created it on 2021/7/29
 */
class ActivityLifecycleMonitor {

    companion object {

        @TestOnly
        fun registerActivityLifecycleCallback(app: Application) {

            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    LogUtils.d("onActivityCreated: $activity")
                }

                override fun onActivityStarted(activity: Activity) {
                    LogUtils.d("onActivityStarted: $activity")

                }

                override fun onActivityResumed(activity: Activity) {
                    LogUtils.d("onActivityResumed: $activity")
                }

                override fun onActivityPaused(activity: Activity) {
                    LogUtils.d("onActivityPaused: $activity")
                }

                override fun onActivityStopped(activity: Activity) {
                    LogUtils.d("onActivityStopped: $activity")
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    LogUtils.d("onActivitySaveInstanceState: $activity")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    LogUtils.d("onActivityDestroyed: $activity")
                }
            })
        }
    }
}