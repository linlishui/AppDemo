package lishui.module.wanandroid.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import lishui.module.wanandroid.R
import lishui.android.ui.extensions.hidden
import lishui.android.ui.extensions.hideKeyboard
import lishui.android.ui.extensions.showKeyboard


class SearchBar(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private val focus: View
    private val textField: TextField
    private val clear: View
    private val cancel: View

    var onSearchListener: ((String?) -> Unit)? = null
    var onImeSearchActionListener: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        View.inflate(context, R.layout.view_search_bar, this)

        focus = findViewById(R.id.focus)
        textField = findViewById(R.id.search_text_field)
        clear = findViewById(R.id.search_clear)
        cancel = findViewById(R.id.search_cancel)

        // listen for text state
        textField.doAfterTextChanged {
            if (it == null) {
                return@doAfterTextChanged
            }
            textFieldChanged(it)
        }

        fun looseFocus() {
            textField.hideKeyboard()
            focus.requestFocus()
        }

        // on back pressed - lose focus and hide keyboard
        textField.onBackPressedListener = {
            // hide keyboard and lose focus
            looseFocus()
        }

        textField.setOnFocusChangeListener { _, hasFocus ->
            cancel.hidden = textField.text.isEmpty() && !hasFocus
        }

        textField.setOnEditorActionListener { _, actionId, event ->
            if (event == null || KeyEvent.ACTION_UP == event.action) {
                looseFocus()
            }
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onImeSearchActionListener?.invoke()
            }
            return@setOnEditorActionListener true
        }

        clear.setOnClickListener {
            textField.setText("")
            // ensure that we have focus when clear is clicked
            if (!textField.hasFocus()) {
                textField.requestFocus()
                // additionally ensure keyboard is showing
                textField.showKeyboard()
            }
        }

        cancel.setOnClickListener {
            textField.setText("")
            looseFocus()
        }

        isSaveEnabled = false
        textField.isSaveEnabled = false
    }

    fun search(text: String) {
        textField.setText(text)
    }

    private fun textFieldChanged(text: CharSequence) {
        val isEmpty = text.isEmpty()
        clear.hidden = isEmpty
        cancel.hidden = isEmpty && !textField.hasFocus()

        onSearchListener?.invoke(if (text.isEmpty()) null else text.toString())
    }
}