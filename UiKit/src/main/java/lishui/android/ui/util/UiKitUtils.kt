package lishui.android.ui.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ContextThemeWrapper
import androidx.annotation.RestrictTo


/**
 *  author : linlishui
 *  time   : 2022/3/23
 *  desc   : UiKit的内部工具类
 */

internal object UiKitUtils {

    @JvmStatic
    fun <T : Activity> findActivity(context: Context): T {
        return when (context) {
            is Activity -> context as T
            is ContextThemeWrapper -> findActivity((context as ContextWrapper).baseContext)
            else -> throw IllegalArgumentException("Cannot find Activity in parent tree")
        }
    }
}