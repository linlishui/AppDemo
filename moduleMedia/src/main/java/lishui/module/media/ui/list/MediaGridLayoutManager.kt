package lishui.module.media.ui.list

import android.content.Context
import android.lib.base.log.LogUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : GridLayoutManager的离屏加载实现
 */
class MediaGridLayoutManager(context: Context, spanCount: Int) : GridLayoutManager(context, spanCount) {

    /**
     * 离屏加载的范围区域，只针对垂直列表滑动。
     */
    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        //super.calculateExtraLayoutSpace(state, extraLayoutSpace)

        val extraSpace = height / 2
        extraLayoutSpace[0] = extraSpace
        extraLayoutSpace[1] = extraSpace
    }
}