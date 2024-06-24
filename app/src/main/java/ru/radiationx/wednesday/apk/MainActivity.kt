package ru.radiationx.wednesday.apk

import android.Manifest
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mintrocket.lib.mintpermissions.MintPermissions
import ru.mintrocket.lib.mintpermissions.ext.isGranted
import ru.radiationx.wednesday.apk.reminder.ReminderScheduler


class MainActivity : AppCompatActivity() {

    private val player = WednesdayPlayer()
    private val popupController = PopupController()

    private var finishJob: Job? = null
    private var playingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<FrameLayout>(R.id.root)
        rootView.keepScreenOn = true
        rootView.doOnLayout {
            val rect = Rect()
            it.getGlobalVisibleRect(rect)
            startWednesday(rect)
        }
    }

    private fun startWednesday(rootRect: Rect) {
        player.init {
            finishPopups(true)
        }
        playingJob = lifecycleScope.launch(Dispatchers.Main.immediate) {
            popupController.warmUp(this@MainActivity)
            player.play(this@MainActivity)

            // await initial frog
            while (player.getPosition() <= BeatConfig.initialPositionMillis) {
                delay(10)
            }

            BeatConfig.plan.forEach { item ->
                popupController.show(this@MainActivity, rootRect, item.config)
                // await next frog
                while (player.getPosition() <= item.positionMillis) {
                    delay(10)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishPopups(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
        popupController.destroy()
    }

    private fun finishPopups(needRemind: Boolean) {
        if (finishJob?.isActive == true) {
            return
        }
        playingJob?.cancel()
        finishJob = lifecycleScope.launch {
            player.destroy()
            popupController.dismiss()
            if (needRemind) {
                askForRemind()
            } else {
                finish()
            }
        }
    }

    private fun askForRemind() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.remind_dialog_title)
            .setMessage(R.string.remind_dialog_message)
            .setPositiveButton(R.string.remind_dialog_positive) { _, _ ->
                lifecycleScope.launch {
                    if (!checkNotificationPermission()) {
                        finishWithoutPermission()
                        return@launch
                    }
                    ReminderScheduler.scheduleNotification(this@MainActivity)
                    finishRemindPositive()
                }
            }
            .setNeutralButton(R.string.remind_dialog_negative) { _, _ ->
                finishRemindNegative()
            }
            .setOnCancelListener {
                finish()
            }
            .show()
    }

    private suspend fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val result = MintPermissions.controller.request(Manifest.permission.POST_NOTIFICATIONS)
        return result.isGranted()
    }

    private fun finishWithoutPermission() {
        Toast.makeText(this, R.string.remind_toast_fail_permission, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun finishRemindPositive() {
        Toast.makeText(this, R.string.remind_toast_positive, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun finishRemindNegative() {
        Toast.makeText(this, R.string.remind_toast_negative, Toast.LENGTH_SHORT).show()
        finish()
    }

}