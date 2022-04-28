package lishui.android.ui.widget.list

import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2022/02/14
 *  desc   : `RecyclerView.ViewHolder`的数据类，viewType 会标识该数据类型。
 */
open class RecyclerData(var viewType: Int = RecyclerView.INVALID_TYPE)