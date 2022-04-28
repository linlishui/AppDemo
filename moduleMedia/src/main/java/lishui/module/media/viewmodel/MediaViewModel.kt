package lishui.module.media.viewmodel

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lishui.module.media.ui.MediaListAdapter
import lishui.module.media.model.ImageDataModel
import lishui.module.media.model.MediaDataModel
import lishui.module.media.model.VideoDataModel
import lishui.lib.base.log.LogUtils

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaViewModel(private val app: Application) : AndroidViewModel(app) {

    private var loadType = ""
    private var loadUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private val originalMediaData = HashMap<String, List<MediaDataModel>?>()
    private val _mediaModelList = MutableLiveData<List<MediaDataModel>>()
    var mediaModelList = _mediaModelList as LiveData<List<MediaDataModel>>

    fun loadMedia(type: String) {
        loadType = type
        loadUri = when (type) {
            IMAGES_INTERNAL -> MediaStore.Images.Media.INTERNAL_CONTENT_URI
            IMAGES_EXTERNAL -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            VIDEOS_INTERNAL -> MediaStore.Video.Media.INTERNAL_CONTENT_URI
            VIDEOS_EXTERNAL -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            FILES_EXTERNAL -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else -> throw IllegalStateException("can not load $type media.")
        }

        originalMediaData[loadType]?.apply {
            _mediaModelList.value = this
            return
        }

        viewModelScope.launch {
            loadMediaByUri(loadUri)
        }
    }

    private suspend fun loadMediaByUri(queryMediaUri: Uri) = withContext(Dispatchers.Default) {
        app.contentResolver.query(
            queryMediaUri,
            null,
            null,
            null,
            "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
        ).use {
            it?.run {
//                this.columnNames.forEach { name ->
//                    LogUtils.d("column name=$name")
//                }
                val mediaDataList = arrayListOf<MediaDataModel>()

                while (it.moveToNext()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val displayName = it.getString(
                        it.getColumnIndexOrThrow(
                            MediaStore.MediaColumns.DISPLAY_NAME
                        )
                    )
                    val dateModified = it.getLong(
                        it.getColumnIndexOrThrow(
                            MediaStore.MediaColumns.DATE_MODIFIED
                        )
                    )
                    val uri = ContentUris.withAppendedId(
                        queryMediaUri, id
                    )
                    val mimeType = it.getString(
                        it.getColumnIndexOrThrow(
                            MediaStore.MediaColumns.MIME_TYPE
                        )
                    )
                    val size = it.getInt(
                        it.getColumnIndexOrThrow(
                            MediaStore.MediaColumns.SIZE
                        )
                    )

                    when (loadType) {
                        IMAGES_INTERNAL, IMAGES_EXTERNAL -> {
                            val imageModel = ImageDataModel(
                                viewType = MediaListAdapter.VIEW_TYPE_IMAGE,
                                id = id,
                                url = uri,
                                displayName = displayName,
                                mimeType = mimeType,
                                size = size,
                                dateModified = dateModified
                            )
                            mediaDataList.add(imageModel)
                        }
                        VIDEOS_INTERNAL, VIDEOS_EXTERNAL -> {
                            // todo: find thumbnail uri if need whilt not use uri directly
                            val imageModel = VideoDataModel(
                                viewType = MediaListAdapter.VIEW_TYPE_VIDEO,
                                id = id,
                                url = uri,
                                displayName = displayName,
                                mimeType = mimeType,
                                size = size,
                                dateModified = dateModified
                            )
                            mediaDataList.add(imageModel)
                        }
                    }
                }
                LogUtils.d("loadMediaByUri:$queryMediaUri, result count=${mediaDataList.size}")
                originalMediaData[loadType] = mediaDataList
                _mediaModelList.postValue(mediaDataList)
            }
        }
    }

    companion object {
        const val IMAGES_INTERNAL = "InternalImages"
        const val IMAGES_EXTERNAL = "ExternalImages"

        const val VIDEOS_INTERNAL = "InternalVideos"
        const val VIDEOS_EXTERNAL = "ExternalVideos"

        const val FILES_EXTERNAL = "ExternalFiles"
    }
}