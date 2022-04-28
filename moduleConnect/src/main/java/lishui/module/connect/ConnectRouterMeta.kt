package lishui.module.connect

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.connect.ui.ChatActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/11/25
 *  desc   : Gitee 模块下路由表元数据配置
 */
class ConnectRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(RouterItem(RouterPath.Connect.CHAT, ChatActivity::class.java, RouteType.ACTIVITY))
        .build()
}