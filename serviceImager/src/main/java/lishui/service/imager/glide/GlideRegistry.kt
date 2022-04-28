package lishui.service.imager.glide

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import lishui.service.imager.core.IRegistry
import lishui.service.imager.core.IRequest

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : Glide的图片加载请求中心站
 */
class GlideRegistry : IRegistry {

    override fun get(context: Context): IRequest {
        return GlideLoaderRequest(context)
    }

    override fun get(activity: FragmentActivity): IRequest {
        return GlideLoaderRequest(activity)
    }

    override fun get(activity: Activity): IRequest {
        return GlideLoaderRequest(activity)
    }

    override fun get(fragment: Fragment): IRequest {
        return GlideLoaderRequest(fragment)
    }

    override fun get(fragment: android.app.Fragment): IRequest {
        return GlideLoaderRequest(fragment)
    }

    override fun get(view: View): IRequest {
        return GlideLoaderRequest(view)
    }

}