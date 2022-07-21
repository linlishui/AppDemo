package lishui.module.media.ui.list

import androidx.recyclerview.widget.RecyclerView
import lishui.service.imager.ImageLoader

/**
 *  author : linlishui
 *  time   : 2022/07/21
 *  desc   :
 */
class MediaListScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                ImageLoader.getLoader().pause(recyclerView.context)
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
                ImageLoader.getLoader().resume(recyclerView.context)
            }
            RecyclerView.SCROLL_STATE_IDLE -> {
                ImageLoader.getLoader().resume(recyclerView.context)
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }
}