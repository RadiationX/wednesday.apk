package ru.radiationx.wednesday.apk

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.TransitionSet
import android.util.SizeF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.repeatCount
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.math.max

class PopupController {

    companion object {
        private const val frogUri = "file:///android_asset/ani1.gif"
    }

    private val createdPopups = mutableListOf<PopupWindow>()

    private val animSizeF = SizeF(272f, 157f)

    private val isNeedTransition = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    private fun createPopup(activity: Activity): PopupWindow {
        val inflater = ContextCompat.getSystemService(activity, LayoutInflater::class.java)
        requireNotNull(inflater)
        val popupView = inflater.inflate(R.layout.dialog_ex, null)
        val popup = PopupWindow(activity).apply {
            contentView = popupView
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            if (isNeedTransition) {
                exitTransition = TransitionSet().apply {
                    addTransition(Explode())
                    addTransition(Fade(Fade.MODE_OUT))
                    ordering = TransitionSet.ORDERING_TOGETHER
                }
            }
        }
        createdPopups.add(popup)
        return popup
    }

    suspend fun warmUp(context: Context) {
        val appContext = context.applicationContext
        val request = ImageRequest.Builder(appContext)
            .data(frogUri)
            .build()
        appContext.imageLoader.execute(request)
    }

    fun show(activity: Activity, rootRect: Rect, config: PopupConfig) {
        val popup = createPopup(activity)
        val popupView = popup.contentView
        val imageView = popupView.findViewById<ImageView>(R.id.imageView)

        val windowWidth = rootRect.width()
        val windowHeight = rootRect.height()

        val scaleX = max(config.size.width / config.size.height, 1f)
        val scaleY = max(config.size.height / config.size.width, 1f)
        val imageWidth = (animSizeF.width * config.size.width).toInt()
        val imageHeight = (animSizeF.height * config.size.height).toInt()

        val x = ((windowWidth - imageWidth) * config.point.x + rootRect.left).toInt()
        val y = ((windowHeight - imageHeight) * config.point.y + rootRect.top).toInt()

        imageView.updateLayoutParams {
            width = imageWidth
            height = imageHeight
        }
        imageView.setBackgroundColor(config.color)
        imageView.scaleX = scaleX
        imageView.scaleY = scaleY
        imageView.load(frogUri) {
            repeatCount(0)
        }
        popup.showAtLocation(activity.window.decorView, Gravity.TOP or Gravity.START, x, y)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun dismissAndAwait() {
        coroutineScope {
            val dismissed = createdPopups.toList().map { popup ->
                async {
                    popup.dismiss()
                    while (popup.contentView.parent != null) {
                        delay(10L)
                    }
                }
            }
            dismissed.awaitAll()
        }
    }

    suspend fun dismiss() {
        if (isNeedTransition) {
            dismissAndAwait()
        } else {
            destroy()
        }
    }

    fun destroy() {
        createdPopups.forEach {
            it.dismiss()
        }
        createdPopups.clear()
    }
}