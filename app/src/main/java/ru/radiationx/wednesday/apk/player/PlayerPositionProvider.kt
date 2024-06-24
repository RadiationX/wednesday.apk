package ru.radiationx.wednesday.apk.player

import android.media.MediaPlayer

class PlayerPositionProvider(
    private val player: MediaPlayer
) : PositionProvider {

    override fun getCurrentPosition(): Long {
        return player.currentPosition.toLong()
    }
}