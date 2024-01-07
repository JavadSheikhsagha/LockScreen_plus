package com.a2mp.lockscreen.LockScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.databinding.ActivityChangeScreenBinding
import me.relex.circleindicator.CircleIndicator3


class ChangeScreenActivity : AppCompatActivity() {

    lateinit var binding : ActivityChangeScreenBinding
    lateinit var viewPager2: ViewPager2
    var sliderHandler: Handler = Handler()
    lateinit var sliderItems: MutableList<LockScreen>
    lateinit var mLockScreenViewModel: LockScreenViewModel
    var idCurrent : Long? = 0
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityChangeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idCurrent = intent.getLongExtra("id",0)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )

        sliderItems = mutableListOf()

        mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)
        mLockScreenViewModel.readAllData.observe(this) {

            for (lockscreen in it) {
                sliderItems.add(lockscreen)
                Log.i("TAGTAGTAGTAGvTAGTAGTAG11111111111", "onCreate: ${lockscreen.id}")
                if (lockscreen.id == idCurrent){
                   position = it.indexOf(lockscreen)
                }
            }

            setupViewPager()

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
        viewPager2.setCurrentItem(position)
        viewPager2.setAdapter(SliderLockAdapter(this ,sliderItems, viewPager2) {
            Log.i("TAGasfasfasfasfasfasfasfasasfasfasfsaffsaffafafsf", "setupViewPager: ${it.id}")
            MainLockScreenData.setSP(this , it)
            var intent = Intent(this , LockScreenActivity::class.java)
            startActivity(intent)
            finish()
        })


        viewPager2.setClipToPadding(false)
        viewPager2.setClipChildren(false)
        viewPager2.setOffscreenPageLimit(3)
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(90))


        viewPager2.setPageTransformer(compositePageTransformer)

        var indicator : CircleIndicator3 = binding.circleIndicator32

        indicator.setViewPager(viewPager2)



    }

    override fun onBackPressed() {
        var intent = Intent(this , LockScreenActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

}