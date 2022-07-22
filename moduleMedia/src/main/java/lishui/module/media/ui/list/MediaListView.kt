package lishui.module.media.ui.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import lishui.module.media.ui.list.MediaListScrollListener

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   : 媒体列表视图
 */
class MediaListView(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    init {
        setHasFixedSize(true)
        setItemViewCacheSize(4)
    }

    private var mediaScrollListener: MediaListScrollListener? = null

//    override fun addOnScrollListener(listener: OnScrollListener) {
//        super.addOnScrollListener(listener)
//        if (listener is MediaListScrollListener) {
//            mediaScrollListener = listener
//        }
//    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
    }

}