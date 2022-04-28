package lishui.service.web.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import lishui.service.web.R
import lishui.service.web.widget.SimpleWebLayout

/**
 * @author lishui.lin
 * Created it on 2021/8/19
 */
class SimpleWebFragment : Fragment(R.layout.simple_web_layout) {

    private lateinit var url: String
    private lateinit var mSimpleWebLayout: SimpleWebLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = requireArguments().getString(WebBrowserActivity.WEB_URL, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSimpleWebLayout = view.findViewById(R.id.simple_browser)
        mSimpleWebLayout.loadUrl(url = url)
    }
}