package ru.radiationx.wednesday.apk.player

import android.os.SystemClock

class TimePositionProvider(
    private val timeOffset: Long
) : PositionProvider {

    private var initialTime = -1L

    override fun getCurrentPosition(): Long {
        if (initialTime == -1L) {
            initialTime = SystemClock.elapsedRealtime()
        }
        return SystemClock.elapsedRealtime() - initialTime + timeOffset
    }
}