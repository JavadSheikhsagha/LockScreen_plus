package com.a2mp.lockscreen.Notification

import android.annotation.TargetApi
import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.R
import java.text.SimpleDateFormat
import java.util.*

class LSNotificationListenerService : NotificationListenerService() {

    lateinit var mNotificationViewModel: NotificationViewModel
    private lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        mNotificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(NotificationViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onNotificationPosted(sbn: StatusBarNotification) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Here you need to play with the sbn object and get the data that you want from it.

            val notification: Notification = sbn.notification
            var time = sbn.notification.`when`

            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val hourAndMinute = formatter.format(time)

            val packageNameeee = sbn.packageName

            Log.i("12d1qf1f3qaff", "onNotificationPosted: ${packageNameeee} ******1")

            val title = sbn.notification.extras.getString(Notification.EXTRA_TITLE)
            val text = sbn.notification.extras.getString(Notification.EXTRA_TEXT)

            var appNameeee = getAppName(packageNameeee)


            if (text?.contains("new messages") == false && text?.contains("messages from") == false &&
                text?.contains("Incoming call") == false && packageNameeee.contains("vpn") == false &&
                appNameeee != "no") {

                Log.i("alsfoaisfhioasfasf", "onNotificationPosted: ${text}")

                mNotificationViewModel.addNotification(
                    Notification(
                        id = System.currentTimeMillis().toString(),
                        appName = appNameeee,
                        packageName = packageNameeee,
                        title = title,
                        content = text,
                        time = hourAndMinute
                    )
                )


            }
            Log.i("12d1qf1f3qaff", "onNotificationPosted: ${title} ******3")
            Log.i("12d1qf1f3qaff", "onNotificationPosted: ${text} ******4")


        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {

        val notification: Notification = sbn.notification
        val packageNamei = sbn.packageName
        var time = sbn.notification.`when`
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val hourAndMinute = formatter.format(time)
        val title = sbn.notification.extras.getString(Notification.EXTRA_TITLE)
        val text = sbn.notification.extras.getString(Notification.EXTRA_TEXT)


        var appName = getAppName(packageNamei)


        try {
            mNotificationViewModel.deleteNotification(
                Notification(
                    id = System.currentTimeMillis().toString(),
                    appName = appName,
                    packageName = packageNamei,
                    title = title,
                    content = text,
                    time = hourAndMinute
                )
            )
        }catch (e:Exception){

        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun onListenerDisconnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Notification listener disconnected - requesting rebind
            requestRebind(ComponentName(this, NotificationListenerService::class.java))
        }
    }

    fun getAppName(packageName: String): String {

        when (packageName) {
            "com.instagram.android" -> {
                return "Instagram"
            }
            "com.facebook.lite", "com.facebook.katana" -> {
                return "Facebook"
            }
            "com.linkedin.android" -> {
                return "LinkedIn"
            }
            "com.twitter.android" -> {
                return "Twitter"
            }
            "com.snapchat.android" -> {
                return "Snapchat"
            }
            "org.telegram.messenger", "org.thunderdog.challegram" -> {
                return "Telegram"
            }
            "com.zhiliaoapp.musically" -> {
                return "TikTok"
            }
            "com.whatsapp", "com.whatsapp.w4b" -> {
                return "WhatsApp"
            }
            "com.tencent.mm" -> {
                return "WeChat"
            }
            "com.discord" -> {
                return "Discord"
            }
            "com.spotify.music" -> {
                return "Spotify"
            }
            "com.google.android.youtube" -> {
                return "YouTube"
            }
            "com.soundcloud.android" -> {
                return "SoundCloud"
            }
            "com.pinterest" -> {
                return "Pinterest"
            }
            "com.tinder", "com.tinder.tinderlite" -> {
                return "Tinder"
            }
            "tv.twitch.android.app" -> {
                return "Twitch"
            }
            "com.google.android.gm" -> {
                return "Gmail"
            }
            "com.android.chrome" -> {
                return "Google Chrome"
            }
            "com.facebook.orca" -> {
                return "Messenger"
            }
            "com.google.android.googlequicksearchbox" -> {
                return "Google"
            }
            "com.samsung.android.dialer", "com.google.android.dialer" -> {
                return "Phone"
            }
            "com.google.android.apps.messaging", "com.messaging.sms" -> {
                return "Messages"
            }
            else -> {
                return "no"
            }
        }

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent =
            Intent(applicationContext, LSNotificationListenerService::class.java)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

}