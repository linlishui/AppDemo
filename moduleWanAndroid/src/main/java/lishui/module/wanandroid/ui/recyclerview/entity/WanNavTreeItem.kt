package lishui.module.wanandroid.ui.recyclerview.entity

import lishui.android.ui.widget.list.IDiffItem
import lishui.android.ui.widget.list.RecyclerData
import lishui.module.wanandroid.ui.util.WanNavTreeViewType

data class WanNavTreeItem constructor(
    val id: Int,
    val name: String,
    val link: String = ""
) : RecyclerData(WanNavTreeViewType.VIEW_TYPE_CHILDREN_NAV_TREE), IDiffItem {

    override fun areItemsTheSame(diffItem: IDiffItem?): Boolean = diffItem?.let {
        if (it !is WanNavTreeItem) {
            return@let false
        }
        return@let this.id == it.id
    } ?: false

    override fun areContentsTheSame(diffItem: IDiffItem?): Boolean = diffItem?.let {
        if (it !is WanNavTreeItem) {
            return@let false
        }
        return@let this == it
    } ?: false

}
