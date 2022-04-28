package lishui.service.imager.core

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable

/**
 *  author : linlishui
 *  time   : 2022/01/20
 *  desc   : 图片请求设置参数
 */
abstract class RequestOptions {

    companion object {

        internal const val UNSET = -1
        internal const val SIZE_MULTIPLIER = 1 shl 1
        internal const val DISK_CACHE_STRATEGY = 1 shl 2
        internal const val PRIORITY = 1 shl 3
        internal const val ERROR_PLACEHOLDER = 1 shl 4
        internal const val ERROR_ID = 1 shl 5
        internal const val PLACEHOLDER = 1 shl 6
        internal const val PLACEHOLDER_ID = 1 shl 7
        internal const val IS_CACHEABLE = 1 shl 8
        internal const val OVERRIDE = 1 shl 9
        internal const val SIGNATURE = 1 shl 10
        internal const val TRANSFORMATION = 1 shl 11
        internal const val RESOURCE_CLASS = 1 shl 12
        internal const val FALLBACK = 1 shl 13
        internal const val FALLBACK_ID = 1 shl 14
        internal const val THEME = 1 shl 15
        internal const val TRANSFORMATION_ALLOWED = 1 shl 16
        internal const val TRANSFORMATION_REQUIRED = 1 shl 17
        internal const val USE_UNLIMITED_SOURCE_GENERATORS_POOL = 1 shl 18
        internal const val ONLY_RETRIEVE_FROM_CACHE = 1 shl 19
        internal const val USE_ANIMATION_POOL = 1 shl 20
    }

    internal var fields = 0

    internal var overrideWidth: Int = UNSET
        set(value) {
            if (value > 0) {
                field = value
            }
        }
    internal var overrideHeight: Int = UNSET
        set(value) {
            if (value > 0) {
                field = value
            }
        }

    internal var isSkipMemoryCache: Boolean = false

    internal var errorPlaceholder: Drawable? = null

    internal var errorId = 0

    internal var placeholderDrawable: Drawable? = null

    internal var placeholderId = 0

    internal fun isSet(flag: Int): Boolean {
        return fields and flag != 0
    }

    abstract fun override(width: Int, height: Int): RequestBuilder

    abstract fun skipMemoryCache(skip: Boolean): RequestBuilder

    abstract fun placeholder(@Nullable drawable: Drawable): RequestBuilder

    abstract fun placeholder(@DrawableRes resourceId: Int): RequestBuilder

    abstract fun error(@Nullable drawable: Drawable): RequestBuilder

    abstract fun error(@DrawableRes resourceId: Int): RequestBuilder

}