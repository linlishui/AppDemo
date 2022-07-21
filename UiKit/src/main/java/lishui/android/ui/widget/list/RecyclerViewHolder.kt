package lishui.android.ui.widget.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : `RecyclerView.ViewHolder`的基础实现类，定义通用的Holder行为
 */
abstract class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup, @LayoutRes layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    var itemData: RecyclerData? = null
        private set

    val itemEventController: RecyclerItemEventController = RecyclerItemEventController()

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int) {
        // 默认空实现。提供给 `AbstractViewHolderFactory` 实现类调用，指示ViewHolder已被创建出来了。
    }

    open fun initViewHolder() {
        // 默认空实现。提供给 `RecyclerAdapter` 调用，用来通知ViewHolder可以进行初始化工作。
    }

    fun bindViewHolder(itemData: RecyclerData?, payloads: List<Any> = emptyList()) {
        this.itemData = itemData
        onBindViewHolder(itemData, payloads)
    }

    abstract fun onBindViewHolder(itemData: RecyclerData?, payloads: List<Any>)

}