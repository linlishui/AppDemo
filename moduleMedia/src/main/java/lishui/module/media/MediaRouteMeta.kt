package lishui.module.media

import lishui.lib.router.table.RouteType
import lishui.lib.router.table.RouterItem
import lishui.lib.router.table.RouterMeta
import lishui.lib.router.table.RouterTable
import lishui.module.media.ui.MediaEntryActivity
import lishui.service.core.router.RouterPath

/**
 *  author : linlishui
 *  time   : 2021/11/29
 *  desc   : 媒体路由元数据
 */
class MediaRouteMeta : RouterMeta {

    override fun buildTable(): RouterTable = RouterTable.Builder()
        .addItem(
            RouterItem(RouterPath.Media.ENTRY, MediaEntryActivity::class.java, RouteType.ACTIVITY)
        ).build()
}