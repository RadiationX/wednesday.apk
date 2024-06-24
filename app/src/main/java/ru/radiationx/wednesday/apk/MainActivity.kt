package ru.radiationx.wednesday.apk

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
            startWednesday(rect)
        }
    }

    private fun startWednesday(rootRect: Rect) {
        flowJob = lifecycleScope.launch {
            frogFlow.start(this@MainActivity, rootRect)
            remindFlow.tryAskForRemind(this@MainActivity)
            finish()
        }
        flowJob?.invokeOnCompletion {
            if (it == null) return@invokeOnCompletion
            val error = when {
                it !is CancellationException -> it
                it.cause != null -> it.cause
                else -> return@invokeOnCompletion
            }
            Log.e("kekeke", "invokeOnCompletion ${error}")
            Toast.makeText(this, error?.message ?: "Unknown error", Toast.LENGTH_SHORT)
                .show()
            finish()
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