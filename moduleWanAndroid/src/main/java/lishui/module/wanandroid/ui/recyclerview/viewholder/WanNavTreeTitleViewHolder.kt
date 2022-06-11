package lishui.module.wanandroid.ui.recyclerview.viewholder

import android.view.ViewGroup
import android.widget.TextView
import lishui.android.ui.widget.list.RecyclerData
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.wanandroid.R
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem

/**
 * @author lishui.lin
 * Created it on 2021/6/7
 */
class WanNavTreeTitleViewHolder(parent: ViewGroup) : RecyclerViewHolder(parent, R.layout.item_wan_nav_tree_title) {

    init {
        itemEventController.enableClick = false
    }

    private val wanNavTitle: TextView = itemView.findViewById(R.id.wan_nav_title_text)

    override fun onBindViewHolder(itemData: RecyclerData?, payloads: List<Any>) {
        val data = itemData?.viewData
        if (data is WanNavTreeItem) {
            wanNavTitle.text = data.name
        }
    }

}