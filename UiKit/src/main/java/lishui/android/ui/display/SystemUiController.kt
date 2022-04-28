package lishui.lib.base.display

import android.view.View
import android.view.Window

/**
 * Utility class to manage various window flags to control system UI.
 */
class SystemUiController(private val mWindow: Window) {

    private var mFlags = 0

    // isLight：状态栏或导航栏是否是亮色背景，当为亮色背景时，会将系统栏调暗，反之亦然
    fun updateUiState(isLight: Boolean) {
        updateUiState(
            if (isLight) FLAG_LIGHT_NAV or FLAG_LIGHT_STATUS else FLAG_DARK_NAV or FLAG_DARK_STATUS
        )
    }

    private fun updateUiState(flags: Int) {
        if (mFlags == flags) {
            return
        }
        mFlags = flags
        val oldFlags = mWindow.decorView.systemUiVisibility
        // Apply the state flags in priority order
        var newFlags = oldFlags
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
            if (mFlags and FLAG_LIGHT_NAV != 0) {
                newFlags = newFlags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else if (mFlags and FLAG_DARK_NAV != 0) {
                newFlags = newFlags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }
        if (mFlags and FLAG_LIGHT_STATUS != 0) {
            newFlags = newFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else if (mFlags and FLAG_DARK_STATUS != 0) {
            newFlags = newFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        if (newFlags != oldFlags) {
            mWindow.decorView.systemUiVisibility = newFlags
        }
    }

    override fun toString(): String = "mFlags=$mFlags"

    companion object {
        const val FLAG_LIGHT_NAV = 1 shl 0
        const val FLAG_DARK_NAV = 1 shl 1
        const val FLAG_LIGHT_STATUS = 1 shl 2
        const val FLAG_DARK_STATUS = 1 shl 3
    }
}