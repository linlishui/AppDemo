package lishui.module.wanandroid.ui.recyclerview.viewholder

import android.view.ViewGroup
import android.widget.TextView
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.wanandroid.R
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem

/**
 * @author lishui.lin
 * Created it on 2021/8/17
 */
class WanNavTreeViewHolder(parent: ViewGroup) : RecyclerViewHolder<WanNavTreeItem>(parent, R.layout.item_wan_nav_tree_layout) {

    private val wanNavText: TextView = itemView.findViewById(R.id.wan_nav_item_text)

    override fun onBindViewHolder(itemData: WanNavTreeItem?, payloads: List<Any>) {
        itemData?.apply {
            wanNavText.text = this.name
            itemView.id = this.id
            itemView.tag = this
        }
    }
}