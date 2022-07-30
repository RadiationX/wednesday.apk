package ru.radiationx.wednesday.apk

import android.graphics.PointF
import android.util.SizeF

data class PopupConfig(
    val point: PointF,
    val size: SizeF,
    val timing: Double,
    val color: Int
)