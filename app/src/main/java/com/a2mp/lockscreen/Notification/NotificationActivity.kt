package com.a2mp.lockscreen.Notification

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.ActivityNotificationBinding
import com.google.firebase.analytics.FirebaseAnalytics

class NotificationActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotificationBinding

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MainLockScreenData.getNotifSwitch(this) == 0){
            binding.switchNotifOnOrOf.isChecked = false
            binding.notifOtherSetttingLl.visibility = View.GONE
        }else{
            binding.switchNotifOnOrOf.isChecked = true
            binding.notifOtherSetttingLl.visibility = View.VISIBLE
        }


            if (MainLockScreenData.getNotifSC(this) == 0){
                binding.showContentNotifOnOrOf.isChecked = false
            }else{
                binding.showContentNotifOnOrOf.isChecked = true
            }


        if (MainLockScreenData.getNotifModel(this) == 0){
            onCountClick()
        }else{
            onListClick()
        }



        binding.switchNotifOnOrOf.setOnClickListener {
            var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            var eventName = "Switch_notification"
            var eventParams = Bundle().apply {
                putString("param_key", "")
                // Add more parameters if needed
            }
            firebaseAnalytics.logEvent(eventName, eventParams)

            if (binding.switchNotifOnOrOf.isChecked){
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val isGranted = notificationManager.isNotificationListenerAccessGranted(
                    ComponentName(this, LSNotificationListenerService::class.java)
                )
                if (isGranted){
                    MainLockScreenData.setNotifSwitch(this , 0)
                    binding.notifOtherSetttingLl.visibility = View.GONE
                }else{
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    startActivity(intent)
                }
            }else{
                MainLockScreenData.setNotifSwitch(this , 1)
                binding.notifOtherSetttingLl.visibility = View.VISIBLE
            }

        }


        binding.countNotifLl.setOnClickListener {
            onCountClick()
            MainLockScreenData.setNotifModel(this , 0)
        }

        binding.listNotifLl.setOnClickListener {
            onListClick()
            MainLockScreenData.setNotifModel(this , 1)
        }

        binding.showContentNotifOnOrOf.setOnClickListener {

            if (!binding.showContentNotifOnOrOf.isChecked){
                MainLockScreenData.setNotifSC(this , 0)
            }else{
                MainLockScreenData.setNotifSC(this , 1)
            }

        }


        binding.notificationBackLl.setOnClickListener {
            finish()
        }


    }



    fun onCountClick(){

        binding.listNotifCv.setBackgroundResource(R.drawable.background_notif_unselected)
        binding.listNotifIv.setImageResource(R.drawable.icon_list_off)
        binding.listNotifTv.setTextColor(Color.parseColor("#353838"))
        binding.countNotifCv.setBackgroundResource(R.drawable.background_notif_selected)
        binding.countNotifIv.setImageResource(R.drawable.icon_count_on)
        binding.countNotifTv.setTextColor(Color.parseColor("#FFFFFF"))

    }


    fun  onListClick(){

        binding.listNotifCv.setBackgroundResource(R.drawable.background_notif_selected)
        binding.listNotifIv.setImageResource(R.drawable.icon_list_on)
        binding.listNotifTv.setTextColor(Color.parseColor("#FFFFFF"))
        binding.countNotifCv.setBackgroundResource(R.drawable.background_notif_unselected)
        binding.countNotifIv.setImageResource(R.drawable.icon_count_off)
        binding.countNotifTv.setTextColor(Color.parseColor("#353838"))

    }

}