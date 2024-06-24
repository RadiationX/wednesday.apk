package ru.radiationx.wednesday.apk.flow

import android.graphics.Rect
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import ru.radiationx.wednesday.apk.config.BeatConfig
import ru.radiationx.wednesday.apk.PopupController
import ru.radiationx.wednesday.apk.WednesdayPlayer

class FrogFlow {

    private val wednesdayPlayer = WednesdayPlayer()
    private val popupController = PopupController()

    suspend fun start(activity: AppCompatActivity, rootRect: Rect) {
        popupController.warmUp(activity)
        wednesdayPlayer.play(activity) { player ->
            showPopupsByBeat(activity, rootRect, player)
        }
        stop()
    }

    suspend fun stop() {
        popupController.dismiss()
    }

    fun destroy() {
        popupController.destroy()
    }

    private suspend fun showPopupsByBeat(
        activity: AppCompatActivity,
        rootRect: Rect,
        player: MediaPlayer
    ) {
        // await initial frog
        while (player.currentPosition <= BeatConfig.initialPositionMillis) {
            delay(10)
        }

        BeatConfig.plan.forEach { item ->
            popupController.show(activity, rootRect, item.config)

            // await next frog
            while (player.currentPosition <= item.positionMillis) {
                delay(10)
            }
        }
    }
}