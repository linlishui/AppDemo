package lishui.module.wanandroid

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.service.core.router.RouterPath
import lishui.module.wanandroid.ui.WanAndroidActivity

/**
 *  author : linlishui
 *  time   : 2021/11/25
 *  desc   : WanAndroid 模块下路由表元数据配置
 */
class WanRouterMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(RouterItem(RouterPath.Wan.ANDROID, WanAndroidActivity::class.java, RouteType.ACTIVITY))
        .build()
}