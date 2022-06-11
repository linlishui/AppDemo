package lishui.android.ui.widget.list

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import java.util.*

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : `RecyclerView.Adapter`的基础实现类，定义通用的Adapter行为。
 *           当 diffCallback 不为空时，此时会进行异步数据项比对和同步刷新。
 */
open class RecyclerAdapter @JvmOverloads constructor(
    private var viewHolderFactory: AbstractViewHolderFactory? = null,
    diffCallback: DiffUtil.ItemCallback<RecyclerData>? = null,
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private val sourceDataList: ArrayList<RecyclerData> = ArrayList()

    private val mDiffer: AsyncListDiffer<RecyclerData>? by lazy {
        if (diffCallback != null) {
            AsyncListDiffer(
                AdapterListUpdateCallback(this),
                AsyncDifferConfig.Builder<RecyclerData>(diffCallback).build()
            )
        } else null
    }

    private var eventMediator: RecyclerEventMediator? = null

    /**
     * @param factory 传入新的ViewHolder工厂实现类
     */
    fun setViewHolderFactory(factory: AbstractViewHolderFactory) {
        this.viewHolderFactory = factory
    }

    fun submitList(newDataList: List<RecyclerData>?) {
        sourceDataList.clear()
        if (newDataList != null) {
            sourceDataList.addAll(newDataList)
        }
        mDiffer?.submitList(sourceDataList) ?: notifyDataSetChanged()
    }

    fun getDataList(): List<RecyclerData> {
        return mDiffer?.currentList ?: sourceDataList
    }

    fun setItemEventMediator(eventMediator: RecyclerEventMediator?) {
        this.eventMediator = eventMediator
    }

    private fun getItemData(position: Int): RecyclerData? {
        if (position in 0 until itemCount) {
            return getDataList()[position]
        }
        return null
    }

    /* 定义一系列 `RecyclerView.Adapter` 中默认实现的方法 */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val recyclerViewHolder = viewHolderFactory?.createViewHolder(parent, viewType)
            ?: throw IllegalStateException("can't find AbstractViewHolderFactory implementation in onCreateViewHolder!")
        recyclerViewHolder.initViewHolder()
        return recyclerViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        onBindViewHolder(holder, position, Collections.emptyList())
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.itemEventController.setupClickMediator(holder.itemView, eventMediator) // 设置点击或长按事件处理
        holder.bindViewHolder(getItemData(position), payloads)
    }

    override fun getItemCount(): Int = getDataList().size

    override fun getItemViewType(position: Int): Int = getItemData(position)?.viewType ?: RecyclerView.INVALID_TYPE
}