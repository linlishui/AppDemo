package lishui.module.wanandroid.ui

import android.view.View
import lishui.lib.router.core.Router
import lishui.module.wanandroid.ui.recyclerview.entity.WanNavTreeItem
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/8/16
 *  desc   : WanAndroid中展现导航内容的fragment
 */
class WanNavFragment : WanNavTreeFragment() {

    override fun loadData() {
        wanViewModel.loadNavTreeData()
    }

    override fun subscribeViewModel() {
        wanViewModel.navDataLiveData.observe(requireActivity()) {
            updateItemList(it)
        }
    }

    override fun onClickView(v: View) {
        val tag = v.tag
        if (tag is WanNavTreeItem) {
            if (tag.link.isNotBlank()) {
                val postcard = Router.getInstance().build(RouterPath.Web.BROWSER)
                postcard.extras.putString(RouterPath.EXTRA_WEB_URL, tag.link)
                postcard.navigation()
            }
        }
    }

    override fun getName() = "Navigation"

}