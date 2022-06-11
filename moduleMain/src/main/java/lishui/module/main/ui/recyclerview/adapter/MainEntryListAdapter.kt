package lishui.module.main.ui.recyclerview.adapter

import android.view.ViewGroup
import lishui.android.ui.widget.list.AbstractViewHolderFactory
import lishui.android.ui.widget.list.RecyclerAdapter
import lishui.android.ui.widget.list.RecyclerData
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.main.ui.recyclerview.model.*
import lishui.module.main.ui.recyclerview.viewholder.MainEntryViewHolder

/**
 * @author lishui.lin
 * Created it on 2021/6/7
 */
class MainEntryListAdapter : RecyclerAdapter(viewHolderFactory) {

    companion object {
        private val viewHolderFactory = object : AbstractViewHolderFactory() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder = MainEntryViewHolder(parent)
        }
    }

    private val entryList = arrayListOf<RecyclerData>()

    init {
        entryList.add(RecyclerData(ChatQuickEntry()))
        entryList.add(RecyclerData(MediaQuickEntry()))
        entryList.add(RecyclerData(GiteeQuickEntry()))
        entryList.add(RecyclerData(ComposeQuickEntry()))
        //entryList.add(RecyclerData(FlutterQuickEntry()))
        entryList.add(RecyclerData(MyServerQuickEntry()))
        entryList.add(RecyclerData(WanAndroidQuickEntry()))
        submitList(entryList)
    }
}