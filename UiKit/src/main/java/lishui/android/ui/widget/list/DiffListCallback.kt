package lishui.android.ui.widget.list

import androidx.recyclerview.widget.DiffUtil

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : 默认 `DiffUtil.Callback` 的实现类
 */
class DiffListCallback(
    private val oldDiffItemList: List<IDiffItem>,
    private val newDiffItemList: List<IDiffItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldDiffItemList.size

    override fun getNewListSize(): Int = newDiffItemList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDiffItemList[oldItemPosition].areItemsTheSame(newDiffItemList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDiffItemList[oldItemPosition].areContentsTheSame(newDiffItemList[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return oldDiffItemList[oldItemPosition].getChangePayload(newDiffItemList[newItemPosition])
    }
}