package lishui.module.main.ui.recyclerview.viewholder

import android.view.ViewGroup
import android.widget.TextView
import lishui.android.ui.delegate.viewBinding
import lishui.android.ui.widget.list.RecyclerData
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.main.R
import lishui.module.main.databinding.ItemMainHomeEntryBinding
import lishui.module.main.ui.recyclerview.model.QuickEntry

/**
 * @author lishui.lin
 * Created it on 2021/6/7
 */
class MainEntryViewHolder(parent: ViewGroup) : RecyclerViewHolder(parent, R.layout.item_main_home_entry) {

    private val mBinding by viewBinding(ItemMainHomeEntryBinding::bind)

    override fun onBindViewHolder(itemData: RecyclerData?, payloads: List<Any>) {
        val viewData = itemData?.viewData
        if (viewData is QuickEntry) {
            itemView.tag = viewData
            mBinding.mainEntryTitle.text = viewData.title
        }
    }
}