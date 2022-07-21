package lishui.module.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import lishui.android.ui.widget.list.AbstractViewHolderFactory
import lishui.android.ui.widget.list.DiffListItemCallback
import lishui.android.ui.widget.list.RecyclerAdapter
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.media.R
import lishui.module.media.ui.holder.MediaViewHolder
import lishui.module.media.ui.util.MediaListUtil.TYPE_IMAGES_EXTERNAL
import lishui.module.media.ui.util.MediaListUtil.TYPE_IMAGES_INTERNAL
import lishui.module.media.ui.util.MediaListUtil.TYPE_NET_IMAGE
import lishui.module.media.ui.util.MediaListUtil.TYPE_NET_VIDEO
import lishui.module.media.ui.util.MediaListUtil.TYPE_VIDEOS_EXTERNAL
import lishui.module.media.ui.util.MediaListUtil.TYPE_VIDEOS_INTERNAL

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaListAdapter : RecyclerAdapter(diffCallback = DiffListItemCallback()) {

    init {
        setViewHolderFactory(object : AbstractViewHolderFactory() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
                val view = when (viewType) {
                    TYPE_IMAGES_INTERNAL, TYPE_IMAGES_EXTERNAL, TYPE_NET_IMAGE -> {
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.item_media_image, parent, false
                        )
                    }
                    TYPE_VIDEOS_INTERNAL, TYPE_VIDEOS_EXTERNAL, TYPE_NET_VIDEO -> {
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.item_media_video, parent, false
                        )
                    }
                    else -> throw IllegalStateException("can't find this viewType=$viewType in MediaDataModel")
                }
                return MediaViewHolder(view)
            }
        })
    }
}