package lishui.service.imager.core

import android.content.Context
import android.view.View

/**
 *  author : linlishui
 *  time   : 2022/01/19
 *  desc   : 定义图片加载器行为
 */
interface ILoader {

    fun getRegistry(): IRegistry

    fun pause(context: Context?)

    fun resume(context: Context?)

    fun clear(view: View?)

    fun clearMemory(context: Context?)

    fun clearDiskCache(context: Context?)

}