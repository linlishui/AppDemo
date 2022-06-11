package lishui.android.ui.widget.list

import android.view.View

/**
 *  author : linlishui
 *  time   : 2022/6/11
 *  desc   : `RecyclerView.ViewHolder` 的事件控制
 */
class RecyclerItemEventController {

    var enableClick: Boolean = true
    var enableLongClick: Boolean = false

    internal fun setupClickMediator(targetView: View?, eventMediator: RecyclerEventMediator?) {
        if (eventMediator == null || (!enableClick && !enableLongClick)) {
            targetView?.setOnClickListener(null)
            targetView?.setOnLongClickListener(null)
            return
        }
        targetView?.setOnClickListener(if (enableClick) eventMediator else null)
        targetView?.setOnLongClickListener(if (enableLongClick) eventMediator else null)
    }

}