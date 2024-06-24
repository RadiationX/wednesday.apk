package ru.radiationx.wednesday.apk.reminder

import android.content.Context
import androidx.core.content.edit
import java.util.Calendar
import java.util.Date

object ReminderDataSource {

    private const val prefName = "data"
    private const val keyAskToRemind = "ask_to_remind"

    fun saveNextAsk(context: Context, date: Date) {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(keyAskToRemind, date.time)
        }
    }

    fun getNextAsk(context: Context): Date {
        val prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return prefs
            .getLong(keyAskToRemind, 0)
            .let { Date(it) }
    }

    fun generateNextRemind(): Date {
        return generateNextCalendar().apply {
            set(Calendar.HOUR_OF_DAY, 9)
        }.time
    }

    fun generateNextAskToRemind(): Date {
        return generateNextCalendar().time
    }

    private fun generateNextCalendar(): Calendar = Calendar.getInstance().apply {
        add(Calendar.WEEK_OF_YEAR, 1)
        set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}