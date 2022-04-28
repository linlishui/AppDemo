package lishui.service.misc.plugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import lishui.lib.base.log.LogUtils
import lishui.service.misc.R

/**
 * @author lishui.lin
 * Created it on 2021/6/17
 */

private const val TAG = "HostActivity"
const val PLUGIN_ACTIVITY_PATH = "plugin_activity_path"

// 插件Activity容器
class HostActivity : Activity() {

    private var pluginActivity: PluginActivity? = null

    private var usePluginRes = false

    companion object {
        @JvmStatic
        fun launchHostPage(context: Context, pluginApkPath: String) {
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra(PLUGIN_ACTIVITY_PATH, pluginApkPath)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usePluginRes = false
        setContentView(R.layout.activity_plugin_host_layout)
        usePluginRes = true

        val pluginPath = intent.getStringExtra(PLUGIN_ACTIVITY_PATH) ?: ""
        if (TextUtils.isEmpty(pluginPath)) {
            LogUtils.d(TAG, "can't not find valid plugin path.")
            finish()
            return
        }

        pluginActivity = PluginManager.loadActivity(pluginPath, this)
        pluginActivity?.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        pluginActivity?.onStart()
    }

    override fun onResume() {
        super.onResume()
        pluginActivity?.onResume()
    }

    override fun onPause() {
        super.onPause()
        pluginActivity?.onPause()
    }

    override fun onStop() {
        super.onStop()
        pluginActivity?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        pluginActivity?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        pluginActivity?.onSaveInstanceState(outState) ?: super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        pluginActivity?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return pluginActivity?.onTouchEvent(event) ?: super.onTouchEvent(event)
    }

    override fun getClassLoader(): ClassLoader {
        return PluginManager.pluginClassLoader ?: super.getClassLoader()
    }

    override fun getResources(): Resources {
        return if (usePluginRes) {
            PluginManager.pluginResources ?: super.getResources()
        } else {
            super.getResources()
        }
    }

}