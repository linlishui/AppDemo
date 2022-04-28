package lishui.service.web.callback

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

/**
 * Javascript invoke Android methods
 *
 * @author lishui.lin
 * Created it on 2021/5/21
 */
class WebMethodsCallback(private val mContext: Context) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}
