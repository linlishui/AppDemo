package lishui.android.ui.widget.list

import android.view.View

/**
 *  author : linlishui
 *  time   : 2022/6/11
 *  desc   : `RecyclerView.Adapter` 的事件分发处理
 */
abstract class RecyclerEventMediator : View.OnClickListener, View.OnLongClickListener {

    override fun onLongClick(v: View?): Boolean = false

}