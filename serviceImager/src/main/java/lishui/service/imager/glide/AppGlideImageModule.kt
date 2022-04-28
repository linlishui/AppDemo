package lishui.service.imager.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule

/**
 *  author : linlishui
 *  time   : 2021/11/30
 *  desc   : Glide模块
 */
@GlideModule(glideName = "DemoGlide")
class AppGlideImageModule : AppGlideModule() {

    companion object {
        private const val DISK_CACHE_SIZE_BYTES = 30 * 1024 * 1024L
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Default Glide cache size is 250MB so make cache smaller at 30MB.
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context, DISK_CACHE_SIZE_BYTES
            )
        )

        // Default # of bitmap pool screens is 4, so reduce to 2 to make room for the additional memory
        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(2f)
            .setMemoryCacheScreens(1.2f)
            .build()
        builder.setMemorySizeCalculator(calculator)
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}