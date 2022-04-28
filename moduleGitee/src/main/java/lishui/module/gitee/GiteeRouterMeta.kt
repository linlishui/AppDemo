package lishui.module.gitee

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.gitee.ui.GiteeActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/11/25
 *  desc   : Gitee 模块下路由表元数据配置
 */
class GiteeRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(RouterItem(RouterPath.Gitee.ENTRY, GiteeActivity::class.java, RouteType.ACTIVITY))
        .build()
}