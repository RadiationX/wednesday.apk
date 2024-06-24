package ru.radiationx.wednesday.apk.config

import android.graphics.PointF
import android.util.SizeF

data class PopupConfig(
    val point: PointF,
    val size: SizeF,
    val timing: Double,
    val color: Int
)