package lishui.module.wanandroid.ui.recyclerview.observer

import androidx.recyclerview.widget.RecyclerView
import lishui.lib.base.log.LogUtils

/**
 *  author : linlishui
 *  time   : 2021/11/18
 *  desc   : WanAndroid中适配器更新数据观察者，暂定为调试使用
 */
class WanAdapterDataObserver : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        LogUtils.d("WanAdapterDataObserver", ">>>>> WanAndroid Adapter onChanged")
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        LogUtils.d(
            "WanAdapterDataObserver",
            ">>>>> WanAndroid Adapter onItemRangeChanged positionStart=$positionStart, itemCount=$itemCount"
        )
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        LogUtils.d(
            "WanAdapterDataObserver",
            ">>>>> WanAndroid Adapter onItemRangeInserted positionStart=$positionStart, itemCount=$itemCount"
        )
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        LogUtils.d(
            "WanAdapterDataObserver",
            ">>>>> WanAndroid Adapter onItemRangeRemoved positionStart=$positionStart, itemCount=$itemCount"
        )
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        LogUtils.d(
            "WanAdapterDataObserver",
            ">>>>> WanAndroid Adapter onItemRangeMoved fromPosition=$fromPosition, toPosition=$toPosition, itemCount=$itemCount"
        )
    }
}