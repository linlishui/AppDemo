package lishui.demo.app.initializer

import android.app.Application
import android.content.Context
import android.lib.base.component.StartupComponent
import android.lib.base.log.LogUtils
import android.lib.base.util.ThreadUtils
import androidx.startup.Initializer
import lishui.demo.app.R
import lishui.lib.router.core.Router

/**
 *  author : linlishui
 *  time   : 2021/11/30
 *  desc   : start-up 组件初始化
 */
class ComponentInitializer : Initializer<Boolean> {

    override fun create(context: Context): Boolean {

        ThreadUtils.executeOnDiskIO {
            Router.init(context)
        }

        val components = context.resources.getStringArray(R.array.initializer_components)
        for (component in components) {

            if (component.isBlank()) continue

            try {
                val app: Application = context as Application
                val clazz = Class.forName(component).asSubclass(StartupComponent::class.java)
                val instance = clazz.newInstance()
                if (instance.isSync()) {
                    ThreadUtils.getUiThreadHandler().post { instance.init(app) }
                } else {
                    ThreadUtils.executeOnComputation { instance.init(app) }
                }
            } catch (ex: Exception) {
                LogUtils.e("ComponentInitializer", "occur error when invoke init() method.", ex)
            }
        }
        return true
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}