package lishui.service.imager.core

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import java.io.File

/**
 *  author : linlishui
 *  time   : 2022/1/19
 *  desc   : 图片请求构建者
 */
abstract class RequestBuilder : RequestOptions() {

    abstract fun load(@Nullable bitmap: Bitmap): RequestBuilder

    abstract fun load(@Nullable drawable: Drawable): RequestBuilder

    abstract fun load(@Nullable string: String): RequestBuilder

    abstract fun load(@Nullable uri: Uri): RequestBuilder

    abstract fun load(@Nullable file: File): RequestBuilder

    abstract fun load(@RawRes @DrawableRes @Nullable resourceId: Int): RequestBuilder

    abstract fun load(@Nullable model: ByteArray): RequestBuilder

    abstract fun load(@Nullable model: Any): RequestBuilder

    abstract fun into(@NonNull imageView: ImageView)
}