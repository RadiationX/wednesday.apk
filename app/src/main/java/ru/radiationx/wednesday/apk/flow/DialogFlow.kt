package ru.radiationx.wednesday.apk.flow

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object DialogFlow {

    suspend fun showDialog(
        context: Context,
        titleRes: Int,
        messageRes: Int,
        positiveRes: Int,
        negativeRes: Int,
    ): Result = suspendCancellableCoroutine { continuation ->
        val dialog = AlertDialog.Builder(context)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(positiveRes) { _, _ ->
                continuation.resume(DialogFlow.Result.Positive)
            }
            .setNegativeButton(negativeRes) { _, _ ->
                continuation.resume(DialogFlow.Result.Negative)
            }
            .setOnCancelListener {
                continuation.resume(DialogFlow.Result.Cancel)
            }
            .show()
        continuation.invokeOnCancellation {
            dialog.cancel()
        }
    }

    enum class Result {
        Positive,
        Negative,
        Cancel
    }
}