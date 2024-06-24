package ru.radiationx.wednesday.apk

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.util.Log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WednesdayPlayer {

    suspend fun play(context: Context, playerScope: suspend (MediaPlayer) -> Unit) {
        val player = MediaPlayer()
        try {
            coroutineScope {
                player.setOnErrorListener { _, what, extra ->
                    player.setOnErrorListener(null)
                    cancel("Player error", Exception("Player error $what, $extra"))
                    true
                }
                awaitPlayingStart(context, player)
                playerScope.invoke(player)
                awaitPlayingComplete(player)
            }
        } finally {
            Log.e("kekeke", "WednesdayPlayer finally")
            player.release()
            player.setOnErrorListener(null)
        }
    }

    private suspend fun awaitPlayingStart(context: Context, player: MediaPlayer) {
        suspendCancellableCoroutine { continuation ->
            val fd = context.assets.openFd("music.mp3")
            player.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                player.playbackParams = PlaybackParams().apply {
                    speed = 4f
                    pitch = 0.5f
                }
            }
            player.setOnPreparedListener {
                Log.e("kekeke", "prepared")
                player.start()
                player.setOnPreparedListener(null)
                continuation.resume(Unit)
            }
            player.prepareAsync()
            continuation.invokeOnCancellation { player.setOnPreparedListener(null) }
        }
    }

    private suspend fun awaitPlayingComplete(player: MediaPlayer) {
        suspendCoroutine { continuation ->
            player.setOnCompletionListener {
                Log.e("kekeke", "media complete")
                player.setOnCompletionListener(null)
                continuation.resume(Unit)
            }
        }
    }
}