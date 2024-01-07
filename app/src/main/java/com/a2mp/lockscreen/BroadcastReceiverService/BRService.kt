package com.a2mp.lockscreen.BroadcastReceiverService

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.SystemClock
import com.a2mp.lockscreen.LockScreen.LockScreenActivity

class BRService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
//        Toast.makeText(this, "Service", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerScreenOffReceiver()
    }

    override fun onDestroy() {
        unregisterReceiver(m_ScreenOffReceiver)
        m_ScreenOffReceiver = null
    }

    private fun registerScreenOffReceiver() {

        m_ScreenOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (action == Intent.ACTION_SCREEN_OFF) {
                    // Do something when power connected



                } else if (action == Intent.ACTION_SCREEN_ON) {
                    // Do something when power disconnected
                    val intent = Intent(this@BRService, LockScreenActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(m_ScreenOffReceiver, filter)

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
            restartServicePendingIntent


    }
    companion object {
        private var m_ScreenOffReceiver: BroadcastReceiver? = null

    }

}