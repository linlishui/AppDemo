package lishui.module.main.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

/**
 * @author lishui.lin
 * Created it on 2021/5/21
 */
class DemoAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->

//            val searchPendingIntent: PendingIntent = Intent(context, BrowserActivity::class.java)
//                .let { intent ->
//                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//                }
//
//            val browsePendingIntent: PendingIntent = Intent(context, BrowserActivity::class.java)
//                .let { intent ->
//                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//                }
//
//            val views: RemoteViews = RemoteViews(
//                context.packageName,
//                R.layout.appwidget_custom_layout
//            ).apply {
//                setOnClickPendingIntent(R.id.widget_search, searchPendingIntent)
//                setOnClickPendingIntent(R.id.widget_browse, browsePendingIntent)
//            }
//
//            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}