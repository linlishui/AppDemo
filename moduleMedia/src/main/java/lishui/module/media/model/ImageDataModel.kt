package lishui.module.media.model

import android.net.Uri

class ImageDataModel(
    viewType: Int,
    id: Long,
    url: Uri,
    size: Int = 0,
    dateModified: Long = 0,
    val mimeType: String = "",
    val displayName: String = ""
) : MediaDataModel(viewType, id, url, size, dateModified)
