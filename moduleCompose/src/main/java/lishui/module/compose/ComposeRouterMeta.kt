package lishui.module.compose

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.compose.ui.ComposeEntryActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2022/02/15
 *  desc   : Compose 模块路由元数据
 */
class ComposeRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(RouterItem(RouterPath.Compose.ENTRY, ComposeEntryActivity::class.java, RouteType.ACTIVITY))
        .build()
}