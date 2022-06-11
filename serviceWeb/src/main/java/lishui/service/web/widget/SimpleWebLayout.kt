package lishui.service.web.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.lib.base.log.LogUtils
import android.lib.base.util.CommonUtils
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.*
import android.widget.FrameLayout
import lishui.service.web.R
import lishui.service.web.callback.WebMethodsCallback
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author lishui.lin
 * Created it on 2021/8/19
 *
 * link layout `simple_browser_layout.xml`
 */
class SimpleWebLayout(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    companion object {

        private const val DEBUG: Boolean = true
        private const val TAG: String = "SimpleWebBrowser"
        private const val JS_INVOKE_ANDROID_NAME = "Android"

        private val WEB_VIEW_VERSION_PATTERN: Pattern =
            Pattern.compile("(Chrome/)([\\d\\.]+)\\s")

        fun debugBrowser(msg: String) {
            if (DEBUG) {
                LogUtils.d(TAG, msg)
            }
        }
    }

    private var mUrl = ""
    private var mWebViewVersion: String = ""

    private lateinit var mWebView: WebView
    private lateinit var mTouchBall: TouchWebBall

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()

        createAndInitializeWebView()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mTouchBall.shouldInterceptTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mTouchBall.onTouchEvent(event)
    }

    private fun initView() {
        mWebView = findViewById(R.id.web_view)
        mTouchBall = findViewById(R.id.touch_ball)

        mTouchBall.setOnClickListener {
            if (mWebView.canGoBack()) {
                mWebView.goBack()
            } else {
                CommonUtils.findActivity<Activity>(context).finish()
            }
        }

        mTouchBall.setOnLongClickListener {
            about()
            true
        }
    }

    private fun createAndInitializeWebView() {
        initializeSettings(mWebView.settings)
        mWebView.webViewClient = SimpleWebViewClient()
        mWebView.webChromeClient = SimpleWebChromeClient()

        // add interface for js
        mWebView.addJavascriptInterface(WebMethodsCallback(context), JS_INVOKE_ANDROID_NAME)
    }

    // setGeolocationDatabasePath deprecated in api level 24,
    // but we still use it because we support api level 19 and up.
    private fun initializeSettings(settings: WebSettings) {
        val appcache: File = context.getDir("appcache", Context.MODE_PRIVATE)
        val geolocation: File = context.getDir("geolocation", Context.MODE_PRIVATE)
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.blockNetworkImage = true

        with(settings) {
            // configure local storage apis and their database paths.
            this.setAppCachePath(appcache.path)
            this.setGeolocationDatabasePath(geolocation.path)
            this.setAppCacheEnabled(true)
            this.setGeolocationEnabled(true)
            this.databaseEnabled = true
            this.domStorageEnabled = true

            // Default layout behavior for chrome on android.
            this.useWideViewPort = true
            this.loadWithOverviewMode = true
            this.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                settings.forceDark = WebSettings.FORCE_DARK_AUTO
            }
        }

        // get web view version
        val matcher: Matcher = WEB_VIEW_VERSION_PATTERN.matcher(settings.userAgentString)
        mWebViewVersion = if (matcher.find()) {
            matcher.group(2) ?: ""
        } else {
            "-"
        }
    }

    fun loadUrl(url: String) {
        mUrl = if (Uri.parse(url).scheme == null) "https://$url" else url
        mWebView.run {
            settings.blockNetworkImage = true
            loadUrl(mUrl)
            requestFocus()
        }
    }

    private fun about() {
        val settings = mWebView.settings
        val summary = StringBuilder()
        summary.append("WebView version : $mWebViewVersion\n")
        for (method in settings.javaClass.methods) {
            if (!methodIsSimpleInspector(method)) continue
            try {
                summary.append(
                    "${method.name} : ${method.invoke(settings)}\n"
                )
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
        }
        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setMessage(summary)
            .setPositiveButton(android.R.string.ok, null)
            .create()
        dialog.show()
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    // Returns true is a method has no arguments and returns either a boolean or a String.
    private fun methodIsSimpleInspector(method: Method): Boolean {
        val returnType: Class<*> = method.returnType
        return ((returnType == Boolean::class.javaPrimitiveType || returnType == String::class.java)
                && method.parameterTypes.isEmpty())
    }

    private inner class SimpleWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            mTouchBall.showProgress()

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mTouchBall.hideProgress()
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {

            val url = request?.url.toString()

            debugBrowser("webViewClient shouldOverrideUrlLoading url=$url")
            view?.hitTestResult?.run {
                if (type == WebView.HitTestResult.UNKNOWN_TYPE && extra.isNullOrEmpty()) {
                    debugBrowser("webViewClient shouldOverrideUrlLoading redirect")
                }
            }

            return if (url.startsWith("http:") || url.startsWith("https:")) {
                view?.loadUrl(url)
                false // 返回false表示此url默认由系统处理,url未加载完成，会继续往下走
            } else {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent)
                } catch (ex: Exception) {
                }
                true
            }
        }
    }

    private inner class SimpleWebChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mTouchBall.setCircleProgress(newProgress)
            if (newProgress == 100) {
                mWebView.settings.blockNetworkImage = false
            }
        }
    }
}