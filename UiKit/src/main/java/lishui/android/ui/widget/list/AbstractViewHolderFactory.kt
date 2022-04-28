package lishui.android.ui.widget.list

import android.view.ViewGroup

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : `RecyclerView.ViewHolder`的抽象工厂类，负责创建出对应的ViewHolder对象
 */
abstract class AbstractViewHolderFactory<DATA> {

    fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<DATA> {
        val viewHolder = onCreateViewHolder(parent, viewType)
        viewHolder.onCreateViewHolder(parent, viewType)
        return viewHolder
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<DATA>

    abstract fun getItemViewType(data: DATA, position: Int): Int
}