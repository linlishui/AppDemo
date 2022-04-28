package lishui.module.media.model

import android.net.Uri

open class MediaDataModel(
    val viewType: Int,
    val id: Long,
    val uri: Uri,
    val size: Int = 0,
    val dateModified: Long = 0
) {

    override fun toString(): String {
        return "MediaDataModel(viewType=$viewType, id=$id, uri=$uri, size=$size, dateModified=$dateModified)"
    }
}
