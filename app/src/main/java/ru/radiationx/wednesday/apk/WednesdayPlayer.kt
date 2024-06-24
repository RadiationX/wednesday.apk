package ru.radiationx.wednesday.apk

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class WednesdayPlayer {

    suspend fun play(context: Context, playerScope: suspend (MediaPlayer) -> Unit) {
        val player = MediaPlayer()
        try {
            coroutineScope {
                launch { awaitError(player) }
                awaitPlayingStart(context, player)
                playerScope.invoke(player)
                awaitPlayingComplete(player)
            }
        } finally {
            Log.e("kekeke", "WednesdayPlayer finally")
            player.release()
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
            continuation.invokeOnCancellation {
                player.setOnPreparedListener(null)
            }
        }
    }

    private suspend fun awaitPlayingComplete(player: MediaPlayer) {
        suspendCancellableCoroutine { continuation ->
            player.setOnCompletionListener {
                Log.e("kekeke", "media complete")
                player.setOnCompletionListener(null)
                continuation.resume(Unit)
            }
            continuation.invokeOnCancellation {
                player.setOnCompletionListener(null)
            }
        }
    }

    private suspend fun awaitError(player: MediaPlayer) {
        suspendCancellableCoroutine<Unit> { continuation ->
            player.setOnErrorListener { _, what, extra ->
                Log.e("kekeke", "media error")
                player.setOnErrorListener(null)
                continuation.resumeWithException(Exception("Player error $what, $extra"))
                true
            }
            continuation.invokeOnCancellation {
                player.setOnErrorListener(null)
            }
        }
    }
}