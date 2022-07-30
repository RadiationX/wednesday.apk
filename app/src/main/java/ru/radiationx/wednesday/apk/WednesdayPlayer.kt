package ru.radiationx.wednesday.apk

import android.content.Context
import android.media.MediaPlayer
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
            player.prepareAsync()
            val listener = MediaPlayer.OnPreparedListener {
                player.start()
                continuation.resume(Unit)
            }
            player.setOnPreparedListener(listener)
            continuation.invokeOnCancellation { player.setOnPreparedListener(null) }
        }
    }

    fun destroy() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

}