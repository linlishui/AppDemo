package lishui.module.media.ui

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import lishui.module.media.R
import lishui.module.media.model.ImageDataModel
import lishui.module.media.model.MediaDataModel
import lishui.module.media.model.VideoDataModel
import lishui.service.imager.ImageLoader

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val imageView: ImageView by lazy {
        // not judge view type, just use even if crash
        itemView.findViewById(R.id.media_thumb_view)
    }

    fun bind(mediaDataModel: MediaDataModel, clickListener: View.OnClickListener?) {
        when (mediaDataModel) {
            is ImageDataModel, is VideoDataModel -> {
                ImageLoader.with(imageView)
                    .load(mediaDataModel.uri)
                    .into(imageView)
            }
            else -> throw IllegalStateException("can't find in MediaDataModel: $mediaDataModel")
        }

        itemView.tag = mediaDataModel
        itemView.setOnClickListener(clickListener)
    }
}