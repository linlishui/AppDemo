package lishui.service.web.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

/**
 * @author lishui.lin
 * Created it on 2021/5/20
 */
class WebBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var targetUrl = intent.data?.toString() ?: ""
        if (TextUtils.isEmpty(targetUrl)) {
            targetUrl = intent.getStringExtra(WEB_URL) ?: "file:///android_asset/js_sendTo_android.html"
        }

        var browserFragment = supportFragmentManager.findFragmentByTag(WEB_TAG)
        if (browserFragment == null) {
            browserFragment = SimpleWebFragment()
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
    }

}