package ru.radiationx.wednesday.apk

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import ru.mintrocket.lib.mintpermissions.ext.initMintPermissions
import ru.radiationx.wednesday.apk.reminder.ReminderNotification

class Application : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    override fun onCreate() {
        super.onCreate()
        initMintPermissions()
        ReminderNotification.createChannel(this)
    }
}
