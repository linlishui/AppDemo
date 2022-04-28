package lishui.module.myserver

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.myserver.ui.MyServerActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : Flutter 模块路由元数据
 */
class MyServerRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(
            RouterItem(RouterPath.MyServer.ENTRY, MyServerActivity::class.java, RouteType.ACTIVITY)
        ).build()
}