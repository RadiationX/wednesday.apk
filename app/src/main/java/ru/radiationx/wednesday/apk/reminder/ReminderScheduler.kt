package ru.radiationx.wednesday.apk.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import ru.radiationx.wednesday.apk.BuildConfig
import java.util.Calendar

object ReminderScheduler {

    private const val REQUEST_CODE = 1234

    fun scheduleNotification(context: Context) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextWednesdayMillis = getNextWednesdayMillis()

        AlarmManagerCompat.setAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            nextWednesdayMillis,
            pendingIntent
        )
    }

    private fun getNextWednesdayMillis(): Long {
        if (BuildConfig.DEBUG) {
            return Calendar.getInstance().apply {
                add(Calendar.SECOND, 10)
            }.timeInMillis
        }
        return Calendar.getInstance().apply {
            add(Calendar.WEEK_OF_YEAR, 1)
            set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }
}