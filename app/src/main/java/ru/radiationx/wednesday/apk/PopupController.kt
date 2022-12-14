package ru.radiationx.wednesday.apk

import android.app.Activity
import android.graphics.Rect
import android.util.SizeF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import coil.load
import coil.request.repeatCount
import kotlin.math.max

class PopupController {

    private val createdPopups = mutableListOf<PopupWindow>()

    private val animSizeF = SizeF(272f, 157f)

    private fun createPopup(activity: Activity): PopupWindow {
        val inflater = ContextCompat.getSystemService(activity, LayoutInflater::class.java)
        requireNotNull(inflater)
        val popupView = inflater.inflate(R.layout.dialog_ex, null)
        val popup = PopupWindow(activity).apply {
            contentView = popupView
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        createdPopups.add(popup)
        return popup
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
        imageView.load("file:///android_asset/ani1.gif") {
            repeatCount(0)
        }
        popup.showAtLocation(activity.window.decorView, Gravity.TOP or Gravity.START, x, y)
    }

    fun destroy() {
        createdPopups.forEach {
            it.dismiss()
        }
        createdPopups.clear()
    }
}