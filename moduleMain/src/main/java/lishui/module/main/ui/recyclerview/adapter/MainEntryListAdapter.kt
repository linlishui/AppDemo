package lishui.module.main.ui.recyclerview.adapter

import android.view.ViewGroup
import lishui.android.ui.widget.list.AbstractViewHolderFactory
import lishui.android.ui.widget.list.RecyclerAdapter
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.main.ui.recyclerview.model.*
import lishui.module.main.ui.recyclerview.viewholder.MainEntryViewHolder

/**
 * @author lishui.lin
 * Created it on 2021/6/7
 */
class MainEntryListAdapter : RecyclerAdapter<QuickEntry>(viewHolderFactory) {

    companion object {
        private val viewHolderFactory = object : AbstractViewHolderFactory<QuickEntry>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<QuickEntry> = MainEntryViewHolder(parent)

            override fun getItemViewType(data: QuickEntry, position: Int): Int {
                return data.viewType
            }
        }
    }

    private val entryList = arrayListOf<QuickEntry>()

    init {
        entryList.add(ChatQuickEntry())
        entryList.add(MediaQuickEntry())
        entryList.add(GiteeQuickEntry())
        entryList.add(ComposeQuickEntry())
        //entryList.add(FlutterQuickEntry())
        entryList.add(MyServerQuickEntry())
        entryList.add(WanAndroidQuickEntry())

        submitList(entryList)
    }
}