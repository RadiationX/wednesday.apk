package ru.radiationx.wednesday.exe

import android.graphics.Color
import android.graphics.PointF
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.util.SizeF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import coil.EventListener
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.load
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.repeatCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis


data class VirginPopup(
    val point: PointF,
    val timing: Double
)

data class ChadPopup(
    val size: SizeF,
    val timing: Double
)


class MainActivity : AppCompatActivity() {
    val mp by lazy { MediaPlayer() }
    val speed = 1.0f

    val kUnitTime: Double = (60000 / 145.0) * (1 / speed);

    val imageLoader by lazy {
        ImageLoader.Builder(this)
            .eventListener(object : EventListener {
                override fun onError(request: ImageRequest, result: ErrorResult) {
                    super.onError(request, result)
                    Log.d("kekeke", "errro", result.throwable)
                }

            })
            .components {
                add(VideoFrameDecoder.Factory())
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    val tlbr = arrayOf(
        VirginPopup(PointF(0.0f, 0.0f), 2.0),
        VirginPopup(PointF(0.1f, 0.1f), 0.5),
        VirginPopup(PointF(0.2f, 0.2f), 0.5),
        VirginPopup(PointF(0.3f, 0.3f), 0.5),
        VirginPopup(PointF(0.4f, 0.4f), 0.5),
        VirginPopup(PointF(0.5f, 0.5f), 0.5),
        VirginPopup(PointF(0.6f, 0.6f), 0.5),
        VirginPopup(PointF(0.7f, 0.7f), 0.5),
        VirginPopup(PointF(0.8f, 0.8f), 0.5),
        VirginPopup(PointF(0.9f, 0.9f), 0.5),
        VirginPopup(PointF(1.0f, 1.0f), 1.5),
    )
    val trbl = arrayOf(
        VirginPopup(PointF(1.0f, 0.0f), 2.0),
        VirginPopup(PointF(0.9f, 0.1f), 0.5),
        VirginPopup(PointF(0.8f, 0.2f), 0.5),
        VirginPopup(PointF(0.7f, 0.3f), 0.5),
        VirginPopup(PointF(0.6f, 0.4f), 0.5),
        VirginPopup(PointF(0.5f, 0.5f), 0.5),
        VirginPopup(PointF(0.4f, 0.6f), 0.5),
        VirginPopup(PointF(0.3f, 0.7f), 0.5),
        VirginPopup(PointF(0.2f, 0.8f), 0.5),
        VirginPopup(PointF(0.1f, 0.9f), 0.5),
        VirginPopup(PointF(0.0f, 1.0f), 1.5),
    )
    val chaos = arrayOf(
        VirginPopup(PointF(0.0f, 0.2f), 2.0),
        VirginPopup(PointF(0.0f, 0.6f), 0.5),
        VirginPopup(PointF(0.3f, 0.4f), 0.5),
        VirginPopup(PointF(0.3f, 0.8f), 0.5),
        VirginPopup(PointF(0.5f, 0.3f), 0.5),
        VirginPopup(PointF(0.5f, 0.6f), 0.5),
        VirginPopup(PointF(0.6f, 0.5f), 0.5),
        VirginPopup(PointF(0.6f, 0.8f), 0.5),
        VirginPopup(PointF(0.8f, 0.5f), 2.5),
    )

    val chungus = arrayOf(
        ChadPopup(SizeF(1.0f, 1.0f), 2.0),
        ChadPopup(SizeF(1.4f, 1.0f), 0.5),
        ChadPopup(SizeF(1.4f, 1.4f), 0.5),
        ChadPopup(SizeF(2.0f, 1.4f), 0.5),
        ChadPopup(SizeF(2.0f, 2.0f), 0.5),
        ChadPopup(SizeF(2.8f, 2.0f), 0.5),
        ChadPopup(SizeF(2.8f, 2.8f), 0.5),
        ChadPopup(SizeF(3.9f, 2.8f), 0.5),
        ChadPopup(SizeF(3.9f, 3.9f), 2.5),
    )

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        lifecycleScope.launch {
            delay(1000)
            showChad(SizeF(1f, 2f), Color.RED)
        }*/

        lifecycleScope.launch(Dispatchers.Main.immediate) {


            audioPlayer("music.wav")
            mp.playbackParams = PlaybackParams().apply {
                speed = this@MainActivity.speed
            }
            delay((3.2f * kUnitTime).toLong())
            //delay(kUnitTime.toLong())
            var timeCorrection = 0L
            tlbr.forEach { virgin ->
                Log.w("kekeke", "correction $timeCorrection")
                timeCorrection = measureTimeMillis {
                    showVirgin(virgin.point, Color.BLUE)
                }
                delay((virgin.timing * kUnitTime - timeCorrection).toLong())
            }


            trbl.forEach { virgin ->
                timeCorrection = measureTimeMillis {
                    showVirgin(virgin.point, Color.RED)
                }
                delay((virgin.timing * kUnitTime - timeCorrection).toLong())

            }

            chaos.forEach { virgin ->
                timeCorrection = measureTimeMillis {
                    showVirgin(virgin.point, Color.BLUE)
                }
                delay((virgin.timing * kUnitTime - timeCorrection).toLong())
            }

            chungus.forEach { chad ->
                timeCorrection = measureTimeMillis {
                    showChad(chad.size, Color.RED)
                }
                delay((chad.timing * kUnitTime - timeCorrection).toLong())
            }
            //to end
            delay(5000)

        }.invokeOnCompletion {
            mp.stop()
        }
    }

    val sizeF = SizeF(272f, 157f)


    private fun createPopup(width: Int, height: Int): PopupWindow {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.dialog_ex, null)
        return PopupWindow(popupView, width, height)
    }

    private fun showVirgin(point: PointF, bgColor: Int) {
        measureTimeMillis {
            val windowWidth = resources.displayMetrics.widthPixels
            val windowHeight = resources.displayMetrics.heightPixels

            val width = sizeF.width.toInt()
            val height = sizeF.height.toInt()
            val popup = createPopup(width, height)

            val x = ((windowWidth - width) * point.x).toInt()
            val y = ((windowHeight - height) * point.y).toInt()

            val popupView = popup.contentView
            val imageView = popupView.findViewById<ImageView>(R.id.imageView)
            imageView.setBackgroundColor(bgColor)
            imageView.load("file:///android_asset/ani1.gif", imageLoader) {
                repeatCount(0)
            }
            popup.showAtLocation(window.decorView, Gravity.TOP or Gravity.START, x, y)
        }.also { Log.d("kekeke", "showVirgin $point, time=$it") }

    }

    private fun showChad(size: SizeF, bgColor: Int) {
        Log.d("kekeke", "showChad $size")
        val popup =
            createPopup(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val popupView = popup.contentView
        val imageView = popupView.findViewById<ImageView>(R.id.imageView)

        imageView.updateLayoutParams {
            width = (sizeF.width).toInt()
            height = (sizeF.height).toInt()
        }
        imageView.setBackgroundColor(bgColor)
        imageView.scaleX = size.width
        imageView.scaleY = size.height
        imageView.load("file:///android_asset/ani1.gif", imageLoader) {
            repeatCount(0)
        }
        popup.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
    }

    private suspend fun audioPlayer(name: String) {
        suspendCancellableCoroutine { continuation ->
            val fd = assets.openFd(name)
            mp.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
            mp.prepareAsync()
            val listener = MediaPlayer.OnPreparedListener {
                mp.start()
                continuation.resume(Unit)
            }
            mp.setOnPreparedListener(listener)
            continuation.invokeOnCancellation { mp.setOnPreparedListener(null) }
        }

    }


    private fun showTestPopup() {
        window.decorView.post {
            // inflate the layout of the popup window
            // inflate the layout of the popup window
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.dialog_ex, null)
            val imageView = popupView.findViewById<ImageView>(R.id.imageView)
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popup = PopupWindow(popupView, width, height)
            popup.showAtLocation(window.decorView, Gravity.TOP or Gravity.LEFT, 0, 0)


            /*val videoPath =  filesDir.absolutePath + "/video2.mp4"
            copy("video2.mp4",videoPath)
            imageView.load(File(videoPath), imageLoader) {
                videoFrameMillis(1000)
            }*/

            imageView.load("file:///android_asset/ani.gif", imageLoader)
            //imageView.load("https://media.giphy.com/media/l1KVcrdl7rJpFnY2s/giphy.gif")

        }
    }
/*
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val params = window.decorView.layoutParams as WindowManager.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        windowManager.updateViewLayout(window.decorView, params)
    }
*/

    private fun copy(from: String, to: String, overrideIfExists: Boolean = false) {
        val outFile = File(to)
        if (outFile.exists()) {
            if (!overrideIfExists) {
                return
            }
        }
        assets.open(from).use { inputStream ->
            FileOutputStream(outFile).use { outStream ->
                inputStream.copyTo(outStream)
            }
        }
    }
}