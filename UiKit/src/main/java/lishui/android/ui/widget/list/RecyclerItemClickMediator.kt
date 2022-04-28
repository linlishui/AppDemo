package lishui.android.ui.widget.list

import android.view.View

/**
 *  author : linlishui
 *  time   : 2022/02/14
 *  desc   : `RecyclerView.ViewHolder` 的事件处理分发
 */
class RecyclerItemClickMediator {

    // 是否启用事件处理
    var enable = true

    var itemClickListener: View.OnClickListener? = null

    var itemLongClickListener: View.OnLongClickListener? = null

    fun setupClickMediator(targetView: View?) {
        targetView?.apply {
            if (enable) {
                this.setOnClickListener(itemClickListener)
                this.setOnLongClickListener(itemLongClickListener)
            } else {
                this.setOnClickListener(null)
                this.setOnLongClickListener(null)
            }
        }
    }
}