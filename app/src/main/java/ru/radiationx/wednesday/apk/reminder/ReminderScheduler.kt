package ru.radiationx.wednesday.apk.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import java.util.Date

object ReminderScheduler {

    private const val REQUEST_CODE = 1234

    fun scheduleNotification(context: Context, date: Date) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        AlarmManagerCompat.setAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            date.time,
            pendingIntent
        )
    }

}