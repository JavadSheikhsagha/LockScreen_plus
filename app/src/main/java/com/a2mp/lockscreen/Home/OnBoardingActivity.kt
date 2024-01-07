package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.ActivityOnBoardingBinding


class OnBoardingActivity : AppCompatActivity() {

    lateinit var binding : ActivityOnBoardingBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MainLockScreenData.getOnBoard(this)){
            var intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }


//        Glide
//            .with(this)
//            .asBitmap()
//            .load(R.raw.onboarding_video)
//            .into(binding.videoSpot)
//

        val video: Uri = Uri.parse("android.resource://com.a2mp.lockscreen/" + R.raw.onboardingvideo)


        binding.videoOnboarding.setVideoURI(video)
        binding.videoOnboarding.setOnPreparedListener(OnPreparedListener { mp ->
            mp.isLooping = true
            binding.videoOnboarding.start()
        })


        binding.onboardingNextBtn.setOnClickListener {
            var intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            MainLockScreenData.setOnBoard(this , true)
            finish()
        }

    }
}