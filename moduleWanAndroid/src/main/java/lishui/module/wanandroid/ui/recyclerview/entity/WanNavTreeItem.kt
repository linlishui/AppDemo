package lishui.module.wanandroid.ui.recyclerview.entity

import lishui.android.ui.widget.list.IDiffItem

data class WanNavTreeItem constructor(
    val id: Int,
    val name: String,
    val link: String = ""
) : IDiffItem {

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
