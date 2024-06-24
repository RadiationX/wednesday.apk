package ru.radiationx.wednesday.apk.flow

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import ru.mintrocket.lib.mintpermissions.MintPermissions
import ru.mintrocket.lib.mintpermissions.ext.isGranted
import ru.radiationx.wednesday.apk.R
import ru.radiationx.wednesday.apk.reminder.ReminderScheduler

class RemindFlow {

    suspend fun askForRemind(context: Context) {
        val result = DialogFlow.showDialog(
            context = context,
            titleRes = R.string.remind_dialog_title,
            messageRes = R.string.remind_dialog_message,
            positiveRes = R.string.remind_dialog_positive,
            negativeRes = R.string.remind_dialog_negative
        )
        when (result) {
            DialogFlow.Result.Positive -> {
                if (checkNotificationPermission()) {
                    ReminderScheduler.scheduleNotification(context)
                    showToast(context, R.string.remind_toast_positive)
                } else {
                    showToast(context, R.string.remind_toast_fail_permission)
                }
            }

            DialogFlow.Result.Negative -> {
                showToast(context, R.string.remind_toast_negative)
            }

            DialogFlow.Result.Cancel -> {
                //do nothing
            }
        }
    }

    private suspend fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val result = MintPermissions.controller.request(Manifest.permission.POST_NOTIFICATIONS)
        return result.isGranted()
    }

    private fun showToast(context: Context, @StringRes messageRes: Int) {
        Toast.makeText(context, messageRes, Toast.LENGTH_SHORT).show()
    }

}