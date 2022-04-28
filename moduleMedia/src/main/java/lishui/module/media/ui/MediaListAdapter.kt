package lishui.module.media.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lishui.module.media.R
import lishui.module.media.model.MediaDataModel

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaListAdapter : RecyclerView.Adapter<MediaViewHolder>() {

    private val mediaDataList = arrayListOf<MediaDataModel>()
    private var clickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = when (viewType) {
            VIEW_TYPE_IMAGE -> {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_media_image, parent, false
                )
            }
            VIEW_TYPE_VIDEO -> {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_media_video, parent, false
                )
            }
            else -> throw IllegalStateException("can't find this viewType=$viewType in MediaDataModel")
        }
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val imageModel = mediaDataList[position]
        holder.bind(imageModel, clickListener)
    }

    override fun getItemCount(): Int = mediaDataList.size

    override fun getItemViewType(position: Int): Int {
        return mediaDataList[position].viewType
    }

    fun setClickListener(clickListener: View.OnClickListener) {
        this.clickListener = clickListener
    }

    fun updateItems(dataModels: List<MediaDataModel>) {
        mediaDataList.clear()
        mediaDataList.addAll(dataModels)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_VIDEO = 2
    }

}