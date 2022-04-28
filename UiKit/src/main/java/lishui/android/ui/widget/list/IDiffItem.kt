package lishui.android.ui.widget.list

/**
 *  author : linlishui
 *  time   : 2022/01/28
 *  desc   : 用于列表比对项的接口。默认情况下，当前项属于`旧项`，而对比的项为`新项`
 */
interface IDiffItem {

    /**
     * @param diffItem 对比的项
     * @return 返回 当前项与比对的项是否唯一标识相等
     */
    fun areItemsTheSame(diffItem: IDiffItem?): Boolean

    /**
     * @param diffItem 对比的项
     * @return 返回 当前项与比对的项的内容是否相同
     */
    fun areContentsTheSame(diffItem: IDiffItem?): Boolean

    /**
     * @param diffItem 对比的项
     * @return 当前项与比对的项属于同一个标识项，返回比对项与当前项存在差异的内容数据
     */
    fun getChangePayload(diffItem: IDiffItem?): Any? {
        return null
    }

}