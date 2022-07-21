package lishui.service.imager.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 *  author : linlishui
 *  time   : 2021/11/30
 *  desc   : Glide模块
 */
@GlideModule(glideName = "AppGlideImager")
class AppGlideImageModule : AppGlideModule() {

    companion object {
        private const val DISK_CACHE_SIZE_BYTES = 50 * 1024 * 1024L
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, DISK_CACHE_SIZE_BYTES))

        // Default # of bitmap pool screens is 4, so reduce to 2 to make room for the additional memory
        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(2f)
            .setMemoryCacheScreens(1.2f)
            .build()
        builder.setMemorySizeCalculator(calculator)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 32
        dispatcher.maxRequestsPerHost = 10
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.dispatcher(dispatcher)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpBuilder.build()))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}