package lishui.service.web

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.service.core.router.RouterPath
import lishui.service.web.ui.WebBrowserActivity

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : Web 服务路由元数据
 */
class WebRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable =
        RouterTable.Builder().addItem(RouterItem(RouterPath.Web.BROWSER, WebBrowserActivity::class.java, RouteType.ACTIVITY)).build()
}