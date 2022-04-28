package lishui.service.misc.plugin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent

/**
 * @author lishui.lin
 * Created it on 2021/6/17
 */
open class PluginActivity(val mHost: Activity) {

    open fun onCreate(savedInstanceState: Bundle?) {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {}

    open fun onDestroy() {}

    open fun onSaveInstanceState(outState: Bundle?) {}

    open fun onTouchEvent(event: MotionEvent?): Boolean = false

    open fun onBackPressed() {}
}