package lishui.service.web.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * @author lishui.lin
 * Created it on 2021/5/20
 */
class WebBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var targetUrl = intent.data?.toString() ?: ""
        if (TextUtils.isEmpty(targetUrl)) {
            targetUrl = intent.getStringExtra(WEB_URL) ?: "www.baidu.com"
            //holder.loadUrl("file:///android_asset/js_sendTo_android.html")
        }

        var browserFragment = supportFragmentManager.findFragmentByTag(WEB_TAG)
        if (browserFragment == null) {
            browserFragment = newInstance<Fragment>(
                X5_BROWSER_FRAGMENT
            ) ?: SimpleWebFragment()
        }
        val bundle = Bundle()
        bundle.putString(WEB_URL, targetUrl)
        browserFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(Window.ID_ANDROID_CONTENT, browserFragment, WEB_TAG)
            .commit()
    }

    companion object {
        const val WEB_TAG = "web_tag"
        const val WEB_URL = "web_url"

        // use system WebView, so add $ in the end
        const val X5_BROWSER_FRAGMENT = "lishui.service.web.ui.X5WebFragment$"

        private fun <T : Any?> newInstance(classPath: String?): T? {
            try {
                val clazz = Class.forName(classPath)
                return clazz.newInstance() as T
            } catch (e: Exception) {
                // no-op
            }
            return null
        }
    }

}