package lishui.module.media.model

import android.net.Uri

class ImageDataModel(
    id: Long,
    url: Uri,
    size: Int = 0,
    dateModified: Long = 0,
    val mimeType: String = "",
    val displayName: String = ""
) : MediaDataModel(id, url, size, dateModified)
