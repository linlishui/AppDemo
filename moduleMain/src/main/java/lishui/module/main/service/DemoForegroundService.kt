package lishui.module.main.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import lishui.module.main.R
import lishui.module.main.ui.activity.MainActivity

/**
 *  author : linlishui
 *  time   : 2022/03/09
 *  desc   : 前台服务demo测试
 */
class DemoForegroundService : Service() {


    companion object {

        private const val ONGOING_NOTIFICATION_ID = 10086

        private const val CHANNEL_ID = "demo_fg_service_channel_id"
        private const val CHANNEL_NAME = "demo_fg_service_channel_name"

        fun start(context: Context) {
            val intent = Intent(context, DemoForegroundService::class.java)
            context.startForegroundService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundWithNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundWithNotification() {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
                if (notificationChannel == null) {
                    val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                    channel.enableLights(false)
                    channel.enableVibration(false)
                    channel.vibrationPattern = null
                    channel.setSound(null, null)
                    notificationManager.createNotificationChannel(channel)
                }
            } catch (e: Exception) {
                // no-op
            }
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentText("Go to MainActivity.")
            .setSmallIcon(R.drawable.ic_baseline_home_24)
            .setContentIntent(pendingIntent)
            .build()

        // Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification)

        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
    }

}