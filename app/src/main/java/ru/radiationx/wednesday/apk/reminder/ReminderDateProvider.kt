package ru.radiationx.wednesday.apk.reminder

import ru.radiationx.wednesday.apk.BuildConfig
import java.util.Calendar
import java.util.Date

object ReminderDateProvider {

    fun getNextRemind(): Date {
        if (BuildConfig.DEBUG) {
            return getNextCalendarDebug().time
        }
        return getNextCalendar().apply {
            set(Calendar.HOUR_OF_DAY, 9)
        }.time
    }

    fun getNextAsk(): Date {
        if (BuildConfig.DEBUG) {
            return getNextCalendarDebug().time
        }
        return getNextCalendar().time
    }

    private fun getNextCalendar(): Calendar = Calendar.getInstance().apply {
        if (get(Calendar.DAY_OF_WEEK) >= Calendar.WEDNESDAY) {
            add(Calendar.WEEK_OF_YEAR, 1)
        }
        set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private fun getNextCalendarDebug(): Calendar = Calendar.getInstance().apply {
        add(Calendar.SECOND, 30)
    }
}