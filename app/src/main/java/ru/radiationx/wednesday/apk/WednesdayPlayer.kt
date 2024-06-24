package ru.radiationx.wednesday.apk

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class WednesdayPlayer {

    suspend fun play(context: Context, playerScope: suspend (PositionProvider) -> Unit) {
        val player = MediaPlayer()
        try {
            coroutineScope {
                val errorJob = launch { awaitError(player) }
                awaitPlayingStart(context, player)
                playerScope.invoke(PlayerPositionProvider(player))
                awaitPlayingComplete(player)
                errorJob.cancel()
            }
        } catch (ex: PlayerException) {
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
            playerScope.invoke(TimePositionProvider(player.currentPosition.toLong()))
        } finally {
            player.release()
        }
    }

    private suspend fun awaitPlayingStart(context: Context, player: MediaPlayer) {
        suspendCancellableCoroutine { continuation ->
            val fd = context.assets.openFd("music.mp3")
            player.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                player.playbackParams = PlaybackParams().apply {
                    speed = 3f
                }
            }
            player.setOnPreparedListener {
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
                player.setOnErrorListener(null)
                continuation.resumeWithException(PlayerException("Player error $what, $extra"))
                true
            }
            continuation.invokeOnCancellation {
                player.setOnErrorListener(null)
            }
        }
    }

    private class PlayerException(override val message: String) : Exception(message)
}