package lishui.service.imager.glide

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import lishui.service.imager.core.ILoader
import lishui.service.imager.core.IRegistry

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : Glide形式的Loader实现
 */
class GlideLoader : ILoader {

    private val glideRegistry: IRegistry = GlideRegistry()

    override fun getRegistry(): IRegistry {
        return glideRegistry
    }

    override fun pause(context: Context?) {
        context?.apply {
            Glide.with(this).pauseRequests()
        }
    }

    override fun resume(context: Context?) {
        context?.apply {
            Glide.with(this).resumeRequests()
        }
    }

    override fun clear(view: View?) {
        view?.apply {
            Glide.with(this).clear(this)
        }
    }

    override fun clearMemory(context: Context?) {
        context?.apply {
            Glide.get(this).clearMemory()
        }
    }

    override fun clearDiskCache(context: Context?) {
        context?.apply {
            Glide.get(this).clearDiskCache()
        }
    }

}