package ru.radiationx.wednesday.apk.reminder

import android.content.Context
import androidx.core.content.edit
import java.util.Date

object ReminderDataSource {

    private const val PREF_NAME = "data"
    private const val KEY_ASK_TO_REMIND = "ask_to_remind"

    fun saveNextAsk(context: Context, date: Date) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putLong(KEY_ASK_TO_REMIND, date.time)
        }
    }

    fun getNextAsk(context: Context): Date {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs
            .getLong(KEY_ASK_TO_REMIND, 0)
            .let { Date(it) }
    }
}