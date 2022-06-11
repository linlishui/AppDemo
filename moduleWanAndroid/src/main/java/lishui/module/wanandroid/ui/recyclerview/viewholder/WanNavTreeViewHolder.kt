package lishui.module.wanandroid.ui.recyclerview.viewholder

import android.view.ViewGroup
import android.widget.TextView
import lishui.android.ui.widget.list.RecyclerData
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.wanandroid.R
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem

/**
 * @author lishui.lin
 * Created it on 2021/8/17
 */
class WanNavTreeViewHolder(parent: ViewGroup) : RecyclerViewHolder(parent, R.layout.item_wan_nav_tree_layout) {

    private val wanNavText: TextView = itemView.findViewById(R.id.wan_nav_item_text)

    override fun onBindViewHolder(itemData: RecyclerData?, payloads: List<Any>) {
        val viewData = itemData?.viewData
        if (viewData is WanNavTreeItem) {
            wanNavText.text = viewData.name
            itemView.id = viewData.id
            itemView.tag = viewData
        }
    }
}