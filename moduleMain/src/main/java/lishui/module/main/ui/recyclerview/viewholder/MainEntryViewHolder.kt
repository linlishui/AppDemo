package lishui.module.main.ui.recyclerview.viewholder

import android.view.ViewGroup
import android.widget.TextView
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.main.R
import lishui.module.main.ui.recyclerview.model.QuickEntry

/**
 * @author lishui.lin
 * Created it on 2021/6/7
 */
class MainEntryViewHolder(parent: ViewGroup) : RecyclerViewHolder<QuickEntry>(parent, R.layout.item_main_home_entry) {

    private val entryTitleView: TextView = itemView.findViewById(R.id.main_entry_title)

    override fun onBindViewHolder(itemData: QuickEntry?, payloads: List<Any>) {
        itemData?.run {
            itemView.tag = this
            entryTitleView.text = this.title
        }
    }
}