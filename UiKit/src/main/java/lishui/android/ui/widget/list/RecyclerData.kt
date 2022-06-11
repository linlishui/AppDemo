package lishui.android.ui.widget.list

/**
 *  author : linlishui
 *  time   : 2022/02/14
 *  desc   : `RecyclerView.ViewHolder`的数据类，viewType 会标识该数据类型。
 */
class RecyclerData @JvmOverloads constructor(
    var viewData: Any?,
    val viewType: Int = 0
) : IDiffItem {

    override fun areItemsTheSame(diffItem: IDiffItem?): Boolean {
        val originItem = viewData
        if (originItem is IDiffItem) {
            return originItem.areItemsTheSame(diffItem)
        }
        return false
    }

    override fun areContentsTheSame(diffItem: IDiffItem?): Boolean {
        val originItem = viewData
        if (originItem is IDiffItem) {
            return originItem.areContentsTheSame(diffItem)
        }
        return false
    }

    override fun getChangePayload(diffItem: IDiffItem?): Any? {
        val originItem = viewData
        if (originItem is IDiffItem) {
            return originItem.getChangePayload(diffItem)
        }
        return null
    }
}