package com.a2mp.lockscreen.MyScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.databinding.ActivityMyScreensBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import me.relex.circleindicator.CircleIndicator3

class MyScreensActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyScreensBinding
    lateinit var viewPager2: ViewPager2
    var sliderHandler: Handler = Handler()
    lateinit var sliderItems: MutableList<LockScreen>
    lateinit var mLockScreenViewModel: LockScreenViewModel
    lateinit var deleteFragment: DeleteFragment
    lateinit var adapteer : SliderAdapter
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMyScreensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderItems = mutableListOf()

//        var adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(this,"ca-app-pub-6545436330357450/2407148865", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//
//                mInterstitialAd = interstitialAd
//            }
//        })

        mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)
        mLockScreenViewModel.readAllData.observe(this) {

            for (lockscreen in it) {
                if (!sliderItems.equals(lockscreen)){
                    sliderItems.add(lockscreen)
                }
            }

            if (sliderItems.size == 0){
                MainLockScreenData.setLSRDEmpty(this , true)
                Toast.makeText(this@MyScreensActivity ,"First add lockscreen", Toast.LENGTH_SHORT).show()
                finish()
            }
            setupViewPager()

        }

        binding.button.setOnClickListener {


//            if (MainLockScreenData.getPurchase(this) != "Active"){
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this)
//                    mInterstitialAd = null
//                } else {
//                    InterstitialAd.load(this,"ca-app-pub-6545436330357450/2407148865", adRequest, object : InterstitialAdLoadCallback() {
//                        override fun onAdFailedToLoad(adError: LoadAdError) {
//
//                            mInterstitialAd = null
//                        }
//
//                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
//
//                            mInterstitialAd = interstitialAd
//                        }
//                    })
//                }
//
//                MainLockScreenData.setSP(this , sliderItems[binding.viewPagerSlider.currentItem])
//                Toast.makeText(this , "Lock screen set successfully" , Toast.LENGTH_SHORT).show()
//            }else{
//
//            }
            MainLockScreenData.setSP(this , sliderItems[binding.viewPagerSlider.currentItem])
            Toast.makeText(this , "Lock screen set successfully" , Toast.LENGTH_SHORT).show()

        }


    }

    private val sliderRunnable =
        Runnable { viewPager2?.setCurrentItem(viewPager2!!.getCurrentItem() + 1) }


    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    fun setupViewPager() {



        viewPager2 = binding.viewPagerSlider

        adapteer = SliderAdapter( this ,sliderItems, viewPager2 , {

            deleteFragment = DeleteFragment( it ){ s ->



                if (sliderItems[s].id == MainLockScreenData.getSP(this).id){
                    MainLockScreenData.setSP(this , sliderItems[0])
                }

                mLockScreenViewModel.deleteLockscreen(sliderItems[s])
                adapteer.updateSlider(s)

            }

            deleteFragment.show(supportFragmentManager , "")

        } , {


        })



        viewPager2.setAdapter(adapteer)

        viewPager2.setClipToPadding(false)
        viewPager2.setClipChildren(false)
        viewPager2.setOffscreenPageLimit(3)
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)


        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(90))


        viewPager2.setPageTransformer(compositePageTransformer)

        var indicator : CircleIndicator3 = binding.circleIndicator33

        indicator.setViewPager(viewPager2)

        if (sliderItems.size == 0){

            binding.button.visibility = View.GONE

        }else{



        }

    }

}