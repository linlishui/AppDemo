package lishui.android.ui.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

var View.hidden: Boolean
    get() = visibility == GONE
    set(value) {
        visibility = if (value) GONE else VISIBLE
    }

fun EditText?.showKeyboard() = this?.context?.getSystemService(InputMethodManager::class.java)?.showSoftInput(this, 0)

fun EditText?.hideKeyboard() = this?.context?.getSystemService(InputMethodManager::class.java)?.hideSoftInputFromWindow(this.windowToken, 0)