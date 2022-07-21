package lishui.module.media.ui.list

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   :
 */
class MediaGridLayoutManager(context: Context, spanCount: Int) : GridLayoutManager(context, spanCount) {

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        //super.calculateExtraLayoutSpace(state, extraLayoutSpace)

        extraLayoutSpace[0] = 2160
        extraLayoutSpace[1] = 2160
    }
}