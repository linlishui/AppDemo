package lishui.android.ui.widget.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import java.util.*

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : `RecyclerView.Adapter`的基础实现类，定义通用的Adapter行为。
 *           当 diffCallback 不为空时，此时会进行异步数据项比对和同步刷新。
 */
open class RecyclerAdapter<DATA> @JvmOverloads constructor(
    private var viewHolderFactory: AbstractViewHolderFactory<DATA>? = null,
    diffCallback: DiffUtil.ItemCallback<DATA>? = null,
) : RecyclerView.Adapter<RecyclerViewHolder<DATA>>() {

    private val sourceDataList: ArrayList<DATA> = ArrayList()

    private val mDiffer: AsyncListDiffer<DATA>? by lazy {
        if (diffCallback != null) {
            AsyncListDiffer(
                AdapterListUpdateCallback(this),
                AsyncDifferConfig.Builder<DATA>(diffCallback).build()
            )
        } else null
    }

    var itemClickListener: View.OnClickListener? = null
    var itemLongClickListener: View.OnLongClickListener? = null

    /**
     * @param factory 传入新的ViewHolder工厂实现类
     */
    fun setViewHolderFactory(factory: AbstractViewHolderFactory<DATA>) {
        this.viewHolderFactory = factory
    }

    fun submitList(newDataList: List<DATA>?) {
        sourceDataList.clear()
        if (newDataList != null) {
            sourceDataList.addAll(newDataList)
        }
        mDiffer?.submitList(sourceDataList) ?: notifyDataSetChanged()
    }

    private fun getCurrentList(): List<DATA> {
        return mDiffer?.currentList ?: sourceDataList
    }

    /* 定义一系列 `RecyclerView.Adapter` 中默认实现的方法 */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<DATA> {
        val recyclerViewHolder = viewHolderFactory?.createViewHolder(parent, viewType)
            ?: throw IllegalStateException("can't find AbstractViewHolderFactory implementation in onCreateViewHolder!")
        recyclerViewHolder.initViewHolder()
        return recyclerViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<DATA>, position: Int) {
        onBindViewHolder(holder, position, Collections.emptyList())
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<DATA>, position: Int, payloads: MutableList<Any>) {
        holder.setItemClickListener(itemClickListener)
        holder.setItemLongClickListener(itemLongClickListener)
        holder.bindViewHolder(sourceDataList[position], payloads)
    }

    override fun getItemCount(): Int = getCurrentList().size

    override fun getItemViewType(position: Int): Int {
        return viewHolderFactory?.getItemViewType(sourceDataList[position], position)
            ?: throw IllegalStateException("can't find AbstractViewHolderFactory implementation in getItemViewType!")
    }
}