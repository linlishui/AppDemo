package lishui.module.media.ui.holder

import android.view.View
import android.widget.ImageView
import lishui.android.ui.widget.list.RecyclerData
import lishui.android.ui.widget.list.RecyclerViewHolder
import lishui.module.media.R
import lishui.module.media.model.MediaDataModel
import lishui.module.media.net.ImageApiData
import lishui.module.media.net.VideoApiData
import lishui.service.imager.ImageLoader

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaViewHolder(view: View) : RecyclerViewHolder(view) {

    private val imageView: ImageView by lazy {
        // not judge view type, just use even if crash
        itemView.findViewById(R.id.media_thumb_view)
    }

    override fun onBindViewHolder(itemData: RecyclerData?, payloads: List<Any>) {
        val viewData = itemData?.viewData ?: return
        itemView.tag = viewData
        var imageUrl = ""
        when (viewData) {
            is MediaDataModel -> {
                imageUrl = viewData.uri.toString()
            }
            is ImageApiData -> {
                imageUrl = viewData.url
            }
            is VideoApiData -> {
                imageUrl = viewData.coverUrl
            }
        }
        ImageLoader.with(imageView)
            .load(imageUrl)
            .placeholder(R.color.uikit_text_disabled)
            .error(R.color.uikit_text_disabled)
            .into(imageView)
    }
}