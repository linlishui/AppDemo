package lishui.android.ui.widget.list

import androidx.recyclerview.widget.DiffUtil

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : 默认 `DiffUtil.ItemCallback` 的实现类
 */
class DiffListItemCallback<DiffItem : IDiffItem> : DiffUtil.ItemCallback<DiffItem>() {

    override fun areItemsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: DiffItem, newItem: DiffItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(oldItem: DiffItem, newItem: DiffItem): Any? {
        return oldItem.getChangePayload(newItem)
    }

}