package lishui.android.ui.display

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.Log
import kotlin.math.roundToInt

object DisplayAutoSize {

    private const val TAG = "DisplayAutoSize"

    private var mOriginalDensity: Float = 0F
    private var mOriginalScaleDensity: Float = 0F

    @JvmStatic
    fun setCustomDensity(activity: Activity, designWidthDpi: Int = 320) {

        val app = activity.application
        val appDisplayMetrics = app.resources.displayMetrics

        if (mOriginalDensity == 0F) {
            mOriginalDensity = appDisplayMetrics.density
            mOriginalScaleDensity = appDisplayMetrics.scaledDensity
            activity.application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig.fontScale > 0) {
                        mOriginalScaleDensity = app.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {
                    // no-op
                }
            })
        }

        val targetDensity: Float = ((appDisplayMetrics.widthPixels / designWidthDpi).toFloat())
        val targetDensityDpi: Int = (targetDensity * DisplayMetrics.DENSITY_MEDIUM).roundToInt()
        val targetScaleDensity: Float = targetDensity * (mOriginalScaleDensity / mOriginalDensity)

        Log.d(TAG, "mOriginalDensity=$mOriginalDensity, mOriginalScaleDensity=$mOriginalScaleDensity, " +
                    "targetDensity=$targetDensity, targetDensityDpi=$targetDensityDpi, targetScaleDensity=$targetScaleDensity")

        with(appDisplayMetrics) {
            this.density = targetDensity
            this.scaledDensity = targetScaleDensity
            this.densityDpi = targetDensityDpi
        }

        with(activity.resources.displayMetrics) {
            this.density = targetDensity
            this.densityDpi = targetDensityDpi
            this.scaledDensity = targetScaleDensity
        }
    }
}