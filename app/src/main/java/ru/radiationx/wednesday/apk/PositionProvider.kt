package ru.radiationx.wednesday.apk

import android.media.MediaPlayer
import android.os.SystemClock

interface PositionProvider {

    fun getCurrentPosition(): Long
}

class PlayerPositionProvider(
    private val player: MediaPlayer
) : PositionProvider {

    override fun getCurrentPosition(): Long {
        return player.currentPosition.toLong()
    }
}

class TimePositionProvider(private val timeOffset: Long) : PositionProvider {

    private var initialTime = -1L

    override fun getCurrentPosition(): Long {
        if (initialTime == -1L) {
            initialTime = SystemClock.elapsedRealtime()
        }
        return SystemClock.elapsedRealtime() - initialTime + timeOffset
    }

}