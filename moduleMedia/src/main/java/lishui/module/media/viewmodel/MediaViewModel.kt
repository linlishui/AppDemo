package lishui.module.media.viewmodel

import android.app.Application
import android.content.ContentUris
import android.lib.base.log.LogUtils
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lishui.android.ui.widget.list.RecyclerData
import lishui.module.media.model.ImageDataModel
import lishui.module.media.model.VideoDataModel
import lishui.module.media.net.task.ImageApiOpenTask
import lishui.module.media.net.task.VideoApiOpenTask
import lishui.module.media.ui.util.MediaListUtil
import lishui.service.net.NetClient

/**
 * @author lishui.lin
 * Created it on 2021/5/31
 */
class MediaViewModel(private val app: Application) : AndroidViewModel(app) {

    private var loadType = MediaListUtil.VIEW_TYPE_UNKNOWN
    private var loadUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    private val originalMediaData = HashMap<Int, List<RecyclerData>?>()
    private val _mediaModelList = MutableLiveData<List<RecyclerData>>()
    var mediaModelList = _mediaModelList as LiveData<List<RecyclerData>>

    fun loadLocalMedia(type: Int) {
        loadType = type

        originalMediaData[loadType]?.apply {
            _mediaModelList.value = this
            return
        }

        loadUri = when (type) {
            MediaListUtil.TYPE_IMAGES_INTERNAL -> MediaStore.Images.Media.INTERNAL_CONTENT_URI
            MediaListUtil.TYPE_IMAGES_EXTERNAL -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaListUtil.TYPE_VIDEOS_INTERNAL -> MediaStore.Video.Media.INTERNAL_CONTENT_URI
            MediaListUtil.TYPE_VIDEOS_EXTERNAL -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaListUtil.TYPE_FILES_EXTERNAL -> MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else -> throw IllegalStateException("can not load $type media.")
        }

        viewModelScope.launch {
            loadLocalMediaByUri(loadUri)
        }
    }

    fun loadNetMedia(type: Int, page: Int) {

        originalMediaData[type]?.apply {
            _mediaModelList.value = this
            return
        }

        when (type) {
            MediaListUtil.TYPE_NET_IMAGE -> {
                viewModelScope.launch {
//                    val task = NetClient.execute(ImageApiOpenTask(page))
//                    val netImageList = task.imageList.map { RecyclerData(it, type) }
//                    originalMediaData[type] = netImageList
//                    _mediaModelList.postValue(netImageList)

                    // todo: 测试多图列表 预加载
                    // 优化方案：https://tristanzeng.github.io/2022/03/09/%E9%A6%96%E9%A1%B5feed%E6%B5%81%E6%BB%91%E5%8A%A8%E5%AE%9E%E7%8E%B0%E5%9B%BE%E7%89%87%E9%A2%84%E5%8A%A0%E8%BD%BD%E5%92%8C%E6%95%B0%E6%8D%AE%E6%97%A0%E6%84%9F%E5%8A%A0%E8%BD%BD%E6%96%B9%E6%A1%88/
                    val task1 = async { NetClient.execute(ImageApiOpenTask(0)) }
                    val task2 = async { NetClient.execute(ImageApiOpenTask(1)) }
                    val task3 = async { NetClient.execute(ImageApiOpenTask(2)) }
                   // val task4 = async { NetClient.execute(ImageApiOpenTask(3)) }
                    //val task5 = async { NetClient.execute(ImageApiOpenTask(4)) }
                    //val task6 = async { NetClient.execute(ImageApiOpenTask(5)) }

                    val netImageList = ArrayList<RecyclerData>()
                    netImageList.addAll(task1.await().imageList.map { RecyclerData(it, type) })
                    netImageList.addAll(task2.await().imageList.map { RecyclerData(it, type) })
                    netImageList.addAll(task3.await().imageList.map { RecyclerData(it, type) })
                    //netImageList.addAll(task4.await().imageList.map { RecyclerData(it, type) })
                    //netImageList.addAll(task5.await().imageList.map { RecyclerData(it, type) })
                    //netImageList.addAll(task6.await().imageList.map { RecyclerData(it, type) })

                    originalMediaData[type] = netImageList
                    _mediaModelList.postValue(netImageList)
                }
            }
            MediaListUtil.TYPE_NET_VIDEO -> {
                viewModelScope.launch {
                    val task1 = async { NetClient.execute(VideoApiOpenTask(0)) }
                    val task2 = async { NetClient.execute(VideoApiOpenTask(1)) }
                    val task3 = async { NetClient.execute(VideoApiOpenTask(2)) }
                    val task4 = async { NetClient.execute(VideoApiOpenTask(3)) }
                    val task5 = async { NetClient.execute(VideoApiOpenTask(4)) }
                    val task6 = async { NetClient.execute(VideoApiOpenTask(5)) }

                    val netVideoList = ArrayList<RecyclerData>()
                    netVideoList.addAll(task1.await().videoList.map { RecyclerData(it, type) })
                    netVideoList.addAll(task2.await().videoList.map { RecyclerData(it, type) })
                    netVideoList.addAll(task3.await().videoList.map { RecyclerData(it, type) })
                    netVideoList.addAll(task4.await().videoList.map { RecyclerData(it, type) })
                    netVideoList.addAll(task5.await().videoList.map { RecyclerData(it, type) })
                    netVideoList.addAll(task6.await().videoList.map { RecyclerData(it, type) })

                    originalMediaData[type] = netVideoList
                    _mediaModelList.postValue(netVideoList)
                }
            }
            else -> throw IllegalStateException("can not load net image with type=$type.")
        }
    }

    private suspend fun loadLocalMediaByUri(queryMediaUri: Uri) = withContext(Dispatchers.Default) {
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
                val mediaDataList = arrayListOf<RecyclerData>()

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
                        MediaListUtil.TYPE_IMAGES_INTERNAL, MediaListUtil.TYPE_IMAGES_EXTERNAL -> {
                            val imageModel = ImageDataModel(
                                id = id,
                                url = uri,
                                displayName = displayName,
                                mimeType = mimeType,
                                size = size,
                                dateModified = dateModified
                            )
                            mediaDataList.add(RecyclerData(imageModel, loadType))
                        }
                        MediaListUtil.TYPE_VIDEOS_INTERNAL, MediaListUtil.TYPE_VIDEOS_EXTERNAL -> {
                            // todo: find thumbnail uri if need whilt not use uri directly
                            val imageModel = VideoDataModel(
                                id = id,
                                url = uri,
                                displayName = displayName,
                                mimeType = mimeType,
                                size = size,
                                dateModified = dateModified
                            )
                            mediaDataList.add(RecyclerData(imageModel, loadType))
                        }
                    }
                }
                LogUtils.d("loadMediaByUri:$queryMediaUri, result count=${mediaDataList.size}")
                originalMediaData[loadType] = mediaDataList
                _mediaModelList.postValue(mediaDataList)
            }
        }
    }
}