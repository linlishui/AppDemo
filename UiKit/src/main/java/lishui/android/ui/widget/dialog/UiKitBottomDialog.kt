package lishui.android.ui.widget.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import lishui.android.ui.R

/**
 *  author : linlishui
 *  time   : 2022/05/23
 *  desc   : 底部弹出页 - 弹窗
 */
abstract class UiKitBottomDialog(context: Context) : AppCompatDialog(context, R.style.UiKit_BottomSheetDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.apply {
            this.setGravity(Gravity.BOTTOM)
            this.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        setCancelable(true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window?.run {
        }
    }

}