package lishui.demo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import lishui.service.core.AppDemo

/**
 *  author : linlishui
 *  time   : 2021/11/15
 *  desc   : start-up 应用初始化任务
 */
class AppInitializer : Initializer<AppDemo> {

    override fun create(context: Context): AppDemo = AppDemo.apply {
        init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(ComponentInitializer::class.java)
    }
}