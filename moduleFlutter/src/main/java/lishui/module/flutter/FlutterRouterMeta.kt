package lishui.module.flutter

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.flutter.ui.FlutterEntryActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/11/26
 *  desc   : Flutter 模块路由元数据
 */
class FlutterRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(RouterItem(RouterPath.Flutter.ENTRY, FlutterEntryActivity::class.java, RouteType.ACTIVITY))
        .build()
}