package lishui.service.imager.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import lishui.service.imager.core.RequestBuilder
import java.io.File

/**
 *  author : linlishui
 *  time   : 2022/1/19
 *  desc   : Glide的图片加载请求构建者
 */
class GlideRequestBuilder<ImageType>(
    private val requestManager: RequestManager,
    private var requestBuilder: com.bumptech.glide.RequestBuilder<ImageType>
) : RequestBuilder() {

    override fun override(width: Int, height: Int): RequestBuilder {
        overrideWidth = width
        overrideHeight = height
        fields = fields or OVERRIDE
        return this
    }

    override fun skipMemoryCache(skip: Boolean): RequestBuilder {
        isSkipMemoryCache = skip
        fields = fields or IS_CACHEABLE
        return this
    }

    override fun placeholder(drawable: Drawable): RequestBuilder {
        placeholderDrawable = drawable
        fields = fields or PLACEHOLDER

        placeholderId = 0
        fields = fields and PLACEHOLDER_ID.inv()
        return this
    }

    override fun placeholder(resourceId: Int): RequestBuilder {
        placeholderId = resourceId
        fields = fields or PLACEHOLDER_ID

        placeholderDrawable = null
        fields = fields and PLACEHOLDER.inv()
        return this
    }

    override fun error(drawable: Drawable): RequestBuilder {
        errorPlaceholder = drawable
        fields = fields or ERROR_PLACEHOLDER

        errorId = 0
        fields = fields and ERROR_ID.inv()
        return this
    }

    override fun error(resourceId: Int): RequestBuilder {
        errorId = resourceId
        fields = fields or ERROR_ID

        errorPlaceholder = null
        fields = fields and ERROR_PLACEHOLDER.inv()
        return this
    }

    override fun load(bitmap: Bitmap): RequestBuilder {
        requestBuilder = requestBuilder.load(bitmap)
        return this
    }

    override fun load(drawable: Drawable): RequestBuilder {
        requestBuilder = requestBuilder.load(drawable)
        return this
    }

    override fun load(string: String): RequestBuilder {
        requestBuilder = requestBuilder.load(string)
        return this
    }

    override fun load(uri: Uri): RequestBuilder {
        requestBuilder = requestBuilder.load(uri)
        return this
    }

    override fun load(file: File): RequestBuilder {
        requestBuilder = requestBuilder.load(file)
        return this
    }

    override fun load(resourceId: Int): RequestBuilder {
        requestBuilder = requestBuilder.load(resourceId)
        return this
    }

    override fun load(model: ByteArray): RequestBuilder {
        requestBuilder = requestBuilder.load(model)
        return this
    }

    override fun load(model: Any): RequestBuilder {
        requestBuilder = requestBuilder.load(model)
        return this
    }

    override fun into(imageView: ImageView) {
        var glideIOptions = com.bumptech.glide.request.RequestOptions()

        if (isSet(ERROR_PLACEHOLDER)) {
            glideIOptions = glideIOptions.error(errorPlaceholder)
        }
        if (isSet(ERROR_ID)) {
            glideIOptions = glideIOptions.error(errorId)
        }
        if (isSet(PLACEHOLDER)) {
            glideIOptions = glideIOptions.placeholder(placeholderDrawable)
        }
        if (isSet(PLACEHOLDER_ID)) {
            glideIOptions = glideIOptions.placeholder(placeholderId)
        }
        if (isSet(IS_CACHEABLE)) {
            glideIOptions = glideIOptions.skipMemoryCache(isSkipMemoryCache)
        }
        if (isSet(OVERRIDE)) {
            glideIOptions = glideIOptions.override(overrideWidth, overrideHeight)
        }
        requestBuilder.apply(glideIOptions).into(imageView)
    }

}