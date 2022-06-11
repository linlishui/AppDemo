package lishui.service.misc.plugin

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Resources
import android.lib.base.log.LogUtils
import dalvik.system.DexClassLoader
import java.io.File

object PluginManager {

    private const val TAG = "PluginManager"
    private const val PLUGIN_FILE_PREFIX = "plugin_app"
    private const val PLUGIN_APK_NAME = "plugin_app-debug.apk"

    private lateinit var app: Application

    private var isInitPlugin = false
    private var pluginApkPath = ""
    private var nativeLibDir = ""
    private var dexOutPath = ""

    var pluginResources: Resources? = null
        private set

    var pluginClassLoader: DexClassLoader? = null
        private set

    fun initApp(app: Application) {
        PluginManager.app = app
        extractPlugin()
    }

    fun loadActivity(activityName: String, hostActivity: HostActivity): PluginActivity? {
        if (!isInitPlugin) {
            return null
        }

        return pluginClassLoader?.run {
            try {
                // activityName 是PluginActivity子类的类路径
                val constructor = this.loadClass(activityName).getConstructor(Activity::class.java)
                val pluginClass = constructor.newInstance(hostActivity)
                return if (pluginClass is PluginActivity) {
                    pluginClass
                } else {
                    null
                }
            } catch (ex: Exception) {
                LogUtils.i(TAG, "can't not create activity for $activityName")
            }
            null
        }
    }

    private fun extractPlugin() {
        if (isInitPlugin) {
            return
        }
        isInitPlugin = true
        Thread {
            var isExistPlugin = false
            var pluginFileName = ""
            app.filesDir.list()?.run {
                for (fileName in this) {
                    if (fileName.startsWith(PLUGIN_FILE_PREFIX)) {
                        isExistPlugin = true
                        pluginFileName = fileName
                        break
                    }
                }
            }

            if (!isExistPlugin) {
                // 找不到插件，使用预置的插件
                val inputStream = app.assets.open(PLUGIN_APK_NAME)
                File(app.filesDir.absolutePath, PLUGIN_APK_NAME).writeBytes(inputStream.readBytes())
                pluginFileName = PLUGIN_APK_NAME
            }

            File(app.filesDir, pluginFileName).run {
                pluginApkPath = this.absolutePath

            }
            File(app.filesDir, "plugin_native_lib").run {
                if (!exists()) {
                    mkdir()
                }
                nativeLibDir = this.absolutePath
            }
            File(app.filesDir, "plugin_dex_out").run {
                if (!exists()) {
                    mkdir()
                }
                dexOutPath = this.absolutePath
            }

            pluginClassLoader =
                DexClassLoader(pluginApkPath, dexOutPath, nativeLibDir, app::class.java.classLoader)

            // 创建插件的Resources
            app.packageManager.getPackageArchiveInfo(
                pluginApkPath,
                PackageManager.GET_ACTIVITIES or
                        PackageManager.GET_META_DATA or
                        PackageManager.GET_SERVICES or
                        PackageManager.GET_PROVIDERS or
                        PackageManager.GET_SIGNATURES
            )?.run {
                applicationInfo.sourceDir = pluginApkPath
                applicationInfo.publicSourceDir = pluginApkPath

                try {
                    pluginResources = app.packageManager.getResourcesForApplication(applicationInfo)
                } catch (ex: Exception) {
                    LogUtils.i(TAG, "can't not create plugin resources")
                }
            }
        }.start()
    }

}