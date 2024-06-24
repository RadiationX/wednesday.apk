package ru.radiationx.wednesday.apk

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class WednesdayPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun init(onCompleted: () -> Unit) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener { onCompleted.invoke() }
    }

    suspend fun play(context: Context) {
        val player = mediaPlayer ?: return
        suspendCancellableCoroutine { continuation ->
            val fd = context.assets.openFd("music.mp3")
            player.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                player.playbackParams = PlaybackParams().apply {
                    speed = 3f
                }
            }
            val listener = MediaPlayer.OnPreparedListener {
                player.start()
                continuation.resume(Unit)
            }
            player.setOnPreparedListener(listener)
            player.prepareAsync()
            continuation.invokeOnCancellation { player.setOnPreparedListener(null) }
        }
    }

    fun getPosition(): Long {
        val player = mediaPlayer ?: return 0L
        return player.currentPosition.toLong()
    }

    fun destroy() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}