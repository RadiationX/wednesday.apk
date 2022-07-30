package ru.radiationx.wednesday.apk

import android.graphics.Color
import android.graphics.PointF
import android.util.SizeF

object BeatConfig {

    private val defaultSize = SizeF(1.0f, 1.0f)
    private val defaultPoint = PointF(0.5f, 0.5f)

    private val cross1 = arrayOf(
        PopupConfig(PointF(0.0f, 0.0f), defaultSize, 2.0, Color.BLUE),
        PopupConfig(PointF(0.1f, 0.1f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.2f, 0.2f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.3f, 0.3f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.4f, 0.4f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.5f, 0.5f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.6f, 0.6f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.7f, 0.7f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.8f, 0.8f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.9f, 0.9f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(1.0f, 1.0f), defaultSize, 1.5, Color.BLUE),
    )
    private val cross2 = arrayOf(
        PopupConfig(PointF(1.0f, 0.0f), defaultSize, 2.0, Color.RED),
        PopupConfig(PointF(0.9f, 0.1f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.8f, 0.2f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.7f, 0.3f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.6f, 0.4f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.5f, 0.5f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.4f, 0.6f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.3f, 0.7f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.2f, 0.8f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.1f, 0.9f), defaultSize, 0.5, Color.RED),
        PopupConfig(PointF(0.0f, 1.0f), defaultSize, 1.5, Color.RED),
    )
    private val chaos = arrayOf(
        PopupConfig(PointF(0.0f, 0.2f), defaultSize, 2.0, Color.BLUE),
        PopupConfig(PointF(0.0f, 0.6f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.3f, 0.4f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.3f, 0.8f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.5f, 0.3f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.5f, 0.6f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.6f, 0.5f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.6f, 0.8f), defaultSize, 0.5, Color.BLUE),
        PopupConfig(PointF(0.8f, 0.5f), defaultSize, 2.5, Color.BLUE),
    )

    private val final = arrayOf(
        PopupConfig(defaultPoint, SizeF(1.0f, 1.0f), 2.0, Color.RED),
        PopupConfig(defaultPoint, SizeF(1.4f, 1.0f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(1.4f, 1.4f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(2.0f, 1.4f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(2.0f, 2.0f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(2.8f, 2.0f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(2.8f, 2.8f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(3.9f, 2.8f), 0.5, Color.RED),
        PopupConfig(defaultPoint, SizeF(3.9f, 3.9f), 2.5, Color.RED),
    )

    const val initialDelay = 3.2f

    const val beatTime: Double = 60000 / 145.0

    val allPopups = cross1 + cross2 + chaos + final
}