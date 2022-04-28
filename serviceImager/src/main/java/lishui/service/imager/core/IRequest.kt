package lishui.service.imager.core

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import java.io.File

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : 图片加载构建的一次请求
 */
interface IRequest {

    fun <ResourceType> asType(resourceClass: Class<ResourceType>): RequestBuilder

    fun asBitmap(): RequestBuilder

    fun asGif(): RequestBuilder

    fun asDrawable(): RequestBuilder

    fun asFile(): RequestBuilder

    /* load系列方法，默认加载类型为Drawable */
    fun load(@Nullable bitmap: Bitmap): RequestBuilder

    fun load(@Nullable drawable: Drawable): RequestBuilder

    fun load(@Nullable string: String): RequestBuilder

    fun load(@Nullable uri: Uri): RequestBuilder

    fun load(@Nullable file: File): RequestBuilder

    fun load(@RawRes @DrawableRes @Nullable resourceId: Int): RequestBuilder

    fun load(@Nullable model: ByteArray): RequestBuilder

    fun load(@Nullable model: Any): RequestBuilder

}