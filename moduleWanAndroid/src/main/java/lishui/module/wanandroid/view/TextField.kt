package lishui.module.wanandroid.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import lishui.lib.base.util.KeyboardUtils

@SuppressLint("AppCompatCustomView")
class TextField(context: Context, attrs: AttributeSet?) : EditText(context, attrs) {
    var onBackPressedListener: (() -> Unit)? = null

    override fun onDetachedFromWindow() {
        onBackPressedListener = null
        super.onDetachedFromWindow()
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (isAttachedToWindow) {
            onBackPressedListener?.also { listener ->
                if (hasFocus()
                        && KeyEvent.KEYCODE_BACK == keyCode
                        && KeyboardUtils.isActionUp(event)) {
                    listener()
                }
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }
}