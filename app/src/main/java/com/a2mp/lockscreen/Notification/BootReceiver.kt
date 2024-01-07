package com.a2mp.lockscreen.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.a2mp.lockscreen.LockScreen.LockscreenService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // start your service here
            val serviceIntent2 = Intent(context, LockscreenService::class.java)
            ContextCompat.startForegroundService(context!!, serviceIntent2)
            context.startService(Intent(context, LSNotificationListenerService::class.java))
        }
    }
}