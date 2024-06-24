package ru.radiationx.wednesday.apk

import android.graphics.Rect
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val player = WednesdayPlayer()
    private val popupController = PopupController()

    private var finishJob: Job? = null
    private var playingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<FrameLayout>(R.id.root)
        rootView.keepScreenOn = true
        rootView.doOnLayout {
            val rect = Rect()
            it.getGlobalVisibleRect(rect)
            startWednesday(rect)
        }
    }

    private fun startWednesday(rootRect: Rect) {
        player.init {
            finishPopups()
        }
        playingJob = lifecycleScope.launch(Dispatchers.Main.immediate) {
            popupController.warmUp(this@MainActivity)
            player.play(this@MainActivity)

            // await initial frog
            while (player.getPosition() <= BeatConfig.initialPositionMillis) {
                delay(10)
            }

            BeatConfig.plan.forEach { item ->
                popupController.show(this@MainActivity, rootRect, item.config)
                // await next frog
                while (player.getPosition() <= item.positionMillis) {
                    delay(10)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishPopups()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.destroy()
        popupController.destroy()
    }

    private fun finishPopups() {
        if (finishJob?.isActive == true) {
            return
        }
        playingJob?.cancel()
        finishJob = lifecycleScope.launch {
            player.destroy()
            popupController.dismiss()
            finish()
        }
    }

}