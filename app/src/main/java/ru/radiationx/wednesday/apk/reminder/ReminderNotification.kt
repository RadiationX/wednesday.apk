package ru.radiationx.wednesday.apk.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import ru.radiationx.wednesday.apk.MainActivity
import ru.radiationx.wednesday.apk.R

object ReminderNotification {

    private const val CHANNEL_ID = "wednesday_reminder"
    private const val NOTIFICATION_ID = 123
    private const val REQUEST_CODE = 123

    fun show(context: Context, title: String, body: String) {
        createChannel(context)
        val notificationManager = context.getSystemService<NotificationManager>() ?: return
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_arrow_back_24)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent(context, Intent(context, MainActivity::class.java)))
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val notificationManager = context.getSystemService<NotificationManager>() ?: return
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val name = context.getString(R.string.notification_channel_name)
            val desc = context.getString(R.string.notification_channel_desc)
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getPendingIntent(context: Context, defaultIntent: Intent): PendingIntent =
        PendingIntent
            .getActivities(
                context,
                REQUEST_CODE,
                arrayOf(defaultIntent),
                PendingIntent.FLAG_MUTABLE
            )


}