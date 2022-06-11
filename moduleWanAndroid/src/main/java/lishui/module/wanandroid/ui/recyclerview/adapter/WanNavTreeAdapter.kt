package lishui.module.wanandroid.ui.recyclerview.adapter

import android.view.ViewGroup
import lishui.android.ui.widget.list.AbstractViewHolderFactory
import lishui.android.ui.widget.list.DiffListItemCallback
import lishui.android.ui.widget.list.RecyclerAdapter
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.wanandroid.ui.recyclerview.viewholder.WanNavTreeTitleViewHolder
import lishui.module.wanandroid.ui.recyclerview.viewholder.WanNavTreeViewHolder
import lishui.module.wanandroid.ui.util.WanNavTreeViewType

/**
 *  author : linlishui
 *  time   : 2021/8/17
 *  desc   : WanAndroid中导航与体系公用的适配器
 */
class WanNavTreeAdapter : RecyclerAdapter(viewHolderFactory, DiffListItemCallback()) {

    companion object {
        private val viewHolderFactory = object : AbstractViewHolderFactory() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder = when (viewType) {
                WanNavTreeViewType.VIEW_TYPE_PARENT_NAV_TREE -> WanNavTreeTitleViewHolder(parent)
                WanNavTreeViewType.VIEW_TYPE_CHILDREN_NAV_TREE -> WanNavTreeViewHolder(parent)
                else -> throw IllegalStateException("can't create view holder in WanNavTreeAdapter.")
            }
        }
    }

}