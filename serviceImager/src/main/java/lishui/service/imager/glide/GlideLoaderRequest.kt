package lishui.service.imager.glide

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import lishui.service.imager.core.IRequest
import lishui.service.imager.core.RequestBuilder
import java.io.File

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : Glide的一次图片加载请求
 */
class GlideLoaderRequest : IRequest {

    private val requestManager: RequestManager

    constructor(context: Context) {
        requestManager = Glide.with(context)
    }

    constructor(activity: FragmentActivity) {
        requestManager = Glide.with(activity)
    }

    constructor(activity: Activity) {
        requestManager = Glide.with(activity)
    }

    constructor(fragment: Fragment) {
        requestManager = Glide.with(fragment)
    }

    constructor(fragment: android.app.Fragment) {
        requestManager = Glide.with(fragment)
    }

    constructor(view: View) {
        requestManager = Glide.with(view)
    }

    override fun <ResourceType> asType(resourceClass: Class<ResourceType>) = GlideRequestBuilder<ResourceType>(
        requestManager = requestManager,
        requestBuilder = requestManager.`as`(resourceClass)
    )

    override fun asBitmap(): RequestBuilder = GlideRequestBuilder<Bitmap>(
        requestManager = requestManager,
        requestBuilder = requestManager.asBitmap()
    )

    override fun asGif(): RequestBuilder = GlideRequestBuilder<GifDrawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asGif()
    )

    override fun asDrawable(): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable()
    )

    override fun asFile(): RequestBuilder = GlideRequestBuilder<File>(
        requestManager = requestManager,
        requestBuilder = requestManager.asFile()
    )

    override fun load(bitmap: Bitmap): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(bitmap)
    )

    override fun load(drawable: Drawable): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(drawable)
    )

    override fun load(string: String): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(string)
    )

    override fun load(uri: Uri): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(uri)
    )

    override fun load(file: File): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(file)
    )

    override fun load(resourceId: Int): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(resourceId)
    )

    override fun load(model: ByteArray): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(model)
    )

    override fun load(model: Any): RequestBuilder = GlideRequestBuilder<Drawable>(
        requestManager = requestManager,
        requestBuilder = requestManager.asDrawable().load(model)
    )
}