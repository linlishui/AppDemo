package lishui.module.media.model

import android.net.Uri
import lishui.android.ui.widget.list.IDiffItem

open class MediaDataModel(
    val id: Long,
    val uri: Uri,
    val size: Int = 0,
    val dateModified: Long = 0
) : IDiffItem {

    override fun areItemsTheSame(diffItem: IDiffItem?): Boolean = diffItem?.let {
        if (it !is MediaDataModel) {
            return@let false
        }
        return@let this.id == it.id
    } ?: false

    override fun areContentsTheSame(diffItem: IDiffItem?): Boolean = diffItem?.let {
        if (it !is MediaDataModel) {
            return@let false
        }
        return@let this.id == it.id && this.uri == it.uri
    } ?: false

    override fun toString(): String {
        return "MediaDataModel(id=$id, uri=$uri, size=$size, dateModified=$dateModified)"
    }
}
