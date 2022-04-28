package lishui.demo.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import lishui.lib.base.log.LogUtils

class DemoAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = serviceInfo
        info.apply {
            // Set the type of events that this service wants to listen to. Others
            // won't be passed to this service.
            /*eventTypes =
                AccessibilityEvent.TYPE_VIEW_CLICKED or AccessibilityEvent.TYPE_VIEW_FOCUSED*/

            // If you only want this service to work with specific applications
            /*packageNames =
                arrayOf("com.android.settings", "com.android.systemui")*/

            // Set the type of feedback your service will provide.
            //feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC

            notificationTimeout = 100
        }

        this.serviceInfo = info
    }

    // 当系统检测到与无障碍服务指定的事件过滤参数匹配的 AccessibilityEvent 时
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d(TAG, "WINDOW STATE=${event.text}{${event.packageName}/${event.className}}")
            }
            /*AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                event.source?.apply {
                    LogUtils.d(TAG, "VIEW CLICKED=${this.text}{${this.className}}")
                }
            }*/
        }
    }

    // 当系统要中断服务正在提供的反馈
    override fun onInterrupt() {
        LogUtils.d(TAG, "invoke onInterrupt method")
    }

    companion object {
        private const val TAG = "DemoAccessibilityService"
    }
}