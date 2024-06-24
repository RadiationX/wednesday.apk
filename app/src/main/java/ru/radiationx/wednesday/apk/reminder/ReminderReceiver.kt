package ru.radiationx.wednesday.apk.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.radiationx.wednesday.apk.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ReminderNotification.show(
            context = context,
            title = context.getString(R.string.notification_title),
            body = context.getString(R.string.notification_body)
        )
    }
}