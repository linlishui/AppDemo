@file:JvmName(name = "AppDemo")
package lishui.service.core

import android.content.Context

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   : 全局任务触发
 */
object AppDemo {

    var isInit = false
        private set

    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        if (isInit) return

        if (!this::appContext.isInitialized)
            appContext = context.applicationContext
        isInit = true
    }
}