package ru.radiationx.wednesday.apk

import android.graphics.Rect
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {

    private val player = WednesdayPlayer()
    private val popupController = PopupController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<FrameLayout>(R.id.root)

        rootView.doOnLayout {
            val rect = Rect()
            it.getGlobalVisibleRect(rect)
            startWednesday(rect)
        }
    }

    private fun startWednesday(rootRect: Rect) {
        player.init {
            finish()
        }
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            player.play(this@MainActivity)

            delay((BeatConfig.initialDelay * BeatConfig.beatTime).toLong())
            var timeCorrection: Long
            BeatConfig.allPopups.forEach {
                timeCorrection = measureTimeMillis {
                    popupController.show(this@MainActivity, rootRect, it)
                }
                delay((it.timing * BeatConfig.beatTime - timeCorrection).toLong())
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
        popupController.destroy()
    }

}