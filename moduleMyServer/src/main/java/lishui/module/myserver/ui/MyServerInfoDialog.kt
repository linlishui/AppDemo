package lishui.module.myserver.ui

import android.content.Context
import android.os.Bundle
import lishui.android.ui.widget.dialog.UiKitBottomDialog
import lishui.module.myserver.R

/**
 *  author : linlishui
 *  time   : 2022/05/23
 *  desc   :
 */
class MyServerInfoDialog(context: Context) : UiKitBottomDialog(context) {

    companion object {
        fun show(context: Context) {
            val infoDialog = MyServerInfoDialog(context)
            infoDialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.my_server_info_layout)
        super.onCreate(savedInstanceState)
    }
}