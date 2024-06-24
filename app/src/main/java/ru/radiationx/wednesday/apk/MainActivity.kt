package ru.radiationx.wednesday.apk

import android.graphics.Rect
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.radiationx.wednesday.apk.flow.FrogFlow
import ru.radiationx.wednesday.apk.flow.RemindFlow


class MainActivity : AppCompatActivity() {

    private val frogFlow = FrogFlow()
    private val remindFlow = RemindFlow()

    private var flowJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<FrameLayout>(R.id.root)
        rootView.keepScreenOn = true
        rootView.doOnLayout {
            val rect = Rect()
            it.getGlobalVisibleRect(rect)
            startFlow(rect)
        }
    }

    private fun startFlow(rootRect: Rect) {
        flowJob = lifecycleScope.launch {
            runCatching {
                frogFlow.start(this@MainActivity, rootRect)
                remindFlow.tryAskForRemind(this@MainActivity)
            }.onSuccess {
                finish()
            }.onFailure {
                if (it is CancellationException) {
                    throw it
                }
                val message = it.message ?: "Unknown error"
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        flowJob?.cancel()
        lifecycleScope.launch {
            frogFlow.stop()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        frogFlow.destroy()
    }
}