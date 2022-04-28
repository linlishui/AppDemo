package lishui.android.ui.widget.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : `RecyclerView.ViewHolder`的基础实现类，定义通用的Holder行为
 */
abstract class RecyclerViewHolder<DATA>(
    parent: ViewGroup,
    @LayoutRes layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    var itemData: DATA? = null
        private set

    private val itemClickMediator: RecyclerItemClickMediator = RecyclerItemClickMediator()

    fun setItemClickMediatorEnable(enable: Boolean) {
        itemClickMediator.enable = enable
    }

    fun setItemClickListener(clickListener: View.OnClickListener?) {
        itemClickMediator.itemClickListener = clickListener
    }

    fun setItemLongClickListener(longClickListener: View.OnLongClickListener?) {
        itemClickMediator.itemLongClickListener = longClickListener
    }

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int) {
        // 默认空实现。提供给 `AbstractViewHolderFactory` 实现类调用，指示ViewHolder已被创建出来了。
    }

    open fun initViewHolder() {
        // 默认空实现。提供给 `RecyclerAdapter` 调用，用来通知ViewHolder可以进行视图初始化工作。
    }

    fun bindViewHolder(itemData: DATA?, payloads: List<Any> = Collections.emptyList()) {
        this.itemData = itemData
        onBindViewHolder(itemData, payloads)

        itemClickMediator.setupClickMediator(this.itemView)  // 设置点击或长按事件处理
    }

    abstract fun onBindViewHolder(itemData: DATA?, payloads: List<Any>)

}