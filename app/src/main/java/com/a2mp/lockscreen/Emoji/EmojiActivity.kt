package com.a2mp.lockscreen.Emoji

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Api.ViewModel.MainViewModel
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.Home.AdOrPurchaseFragment
import com.a2mp.lockscreen.Home.PurchaseActivity
import com.a2mp.lockscreen.Home.REWARDED_AD_3
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.MyScreens.SliderAdapter
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.SetLockScreen.EditBottomFragment
import com.a2mp.lockscreen.Widgets.*
import com.a2mp.lockscreen.databinding.ActivityEmojiBinding
import com.a2mp.lockscreen.databinding.ActivitySetLockScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.tonyodev.fetch2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

val liveData = MutableLiveData<String>()

class EmojiActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmojiBinding
    lateinit var emojiBackgroundFragment: EmojiBackgroundFragment
    lateinit var emojiBottomSheetFragment: EmojiBottomSheetFragment
    private val viewModel by viewModels<MainViewModel>()
    lateinit var mLockScreenViewModel: LockScreenViewModel
    var token = "18e16c0f9e0e80bdf0af3f970ef1bbc7aa9352b379a184f0f4eafeb84f92dff7"
    lateinit var editBottomFragment: EditBottomFragment
    var fontSelected: Int? = R.font.roboto_medium
    var colorSelected: String? = "#FFFFFF"
    private var mRewardedAd: RewardedAd? = null
    lateinit var setWidgetsBttomFragment: SetWidgetsBottomFragment
    lateinit var widgetIsFullFragment: WidgetIsFullFragment
    var widgetAdded = 0
    lateinit var widgetSelected: MutableList<WidgetModel>
    lateinit var setWidgetsTopFragment: SetWidgetsTopFragment
    var topWidget = TopWidgetItems(0, 0, "${getDate(System.currentTimeMillis())}", "Date")
    var widgetsUtility = WidgetsUtility()
    lateinit var startBackColor: String
    lateinit var startEmojies: MutableList<String>
    lateinit var adOrPurchaseFragment: AdOrPurchaseFragment
    var flagHasSeen3Ad = "no"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmojiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var fromEmojiRecycler = intent.getBooleanExtra("recycler" , false)


        if (fromEmojiRecycler){
            startBackColor = intent.getStringExtra("background")!!
            startEmojies = mutableListOf()
            for (emojiw in intent.getStringExtra("emojies")!!.split(",")?.toMutableList()){
                startEmojies.add(emojiw)
                Log.i("asljfsalkjsaflbsljf", "onCreate: $emojiw")
            }
        }else{
            startBackColor = "#666666"
            startEmojies = mutableListOf()
            startEmojies.add("\uD83D\uDE00")
            startEmojies.add("\uD83D\uDE03")
        }

        binding.imageBackooE.setBackgroundColor(Color.parseColor(startBackColor))
        setClock()
        widgetSelected = mutableListOf()
        var adRequest = AdRequest.Builder().build()

        mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)

        if (MainLockScreenData.getPurchase(this) == "Active") {
            binding.mirrorCvE.visibility = View.GONE
            binding.gifImageViewMirrorE.visibility = View.GONE
            binding.diamondECv.visibility = View.GONE
        } else {
            binding.gifImageViewMirrorE.visibility = View.VISIBLE
            binding.gifImageViewMirrorE.repeatCount = 280
            binding.gifImageViewMirrorE.playAnimation()
        }

        setNewDataForWidget()







        binding.topWidgetTvE.text = topWidget.detail

        editBottomFragment = EditBottomFragment({ color, font ->
            binding.clockTvSlse.setTextColor(Color.parseColor(color))
            val typeface = ResourcesCompat.getFont(this, font)
            binding.clockTvSlse.setTypeface(typeface)
            colorSelected = color
            fontSelected = font
        }, {
            val typeface = ResourcesCompat.getFont(this, it)
            binding.clockTvSlse.setTypeface(typeface)
            fontSelected = it
        }, {
            binding.clockTvSlse.setTextColor(Color.parseColor(it))
            colorSelected = it
        }, {
            setBorders()
            binding.addWidgetLlTextE.visibility = View.VISIBLE
            setButtonVisible()
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvE.visibility = View.GONE
                binding.gifImageViewMirrorE.visibility = View.GONE
                binding.diamondECv.visibility = View.GONE
            } else {

                if (flagHasSeen3Ad == "no") {
                    binding.mirrorCvE.visibility = View.VISIBLE
                    binding.gifImageViewMirrorE.visibility = View.VISIBLE
                    binding.diamondECv.visibility = View.VISIBLE
                }

            }
        })


        widgetIsFullFragment = WidgetIsFullFragment()


        setWidgetsBttomFragment = SetWidgetsBottomFragment({ wm ->

            if (widgetAdded + wm.sizoo <= 4) {

                widgetAdded += wm.sizoo
                widgetSelected.add(wm)
                binding.addWidgetLlE.visibility = View.GONE

                val view = LayoutInflater.from(binding.widgetsLlE.context)
                    .inflate(wm.view, binding.widgetsLlE, false)
                binding.widgetsLlE.setOnClickListener { null }
                binding.widgetsLlE.isClickable = false
                try {
                    widgetsUtility.setBottomWidgetDetails(view, this, wm.name)
                } catch (e: Exception) {

                }
                binding.widgetsLlE.addView(view)

                view.findViewById<CardView>(R.id.widget_remove_cv).setOnClickListener {
                    binding.widgetsLlE.removeView(view)
                    widgetSelected.remove(wm)
                    widgetAdded -= wm.sizoo
                    Log.i("aslfnqowiroiqwr3324", "onCreate: ${widgetAdded} \t ${wm.sizoo}")
                    if (widgetAdded == 0) {
                        binding.addWidgetLlE.visibility = View.VISIBLE
                        binding.widgetsLlE.setOnClickListener {

                            if (!setWidgetsBttomFragment.isAdded) {
                                setWidgetsBttomFragment.show(supportFragmentManager, "")
                                binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                                binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                            }

                        }
                    }
                }

                view.setOnClickListener {
                    if (!setWidgetsBttomFragment.isAdded) {
                        setWidgetsBttomFragment.show(supportFragmentManager, "")
                        binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                        binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                    }
                }

                try {
                    view.findViewById<ImageView>(R.id.click_here)?.setOnClickListener {
                        if (!setWidgetsBttomFragment.isAdded) {
                            setWidgetsBttomFragment.show(supportFragmentManager, "")
                            binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                            binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                        }
                    }
                } catch (e: Exception) {

                }


            } else {
                if (!widgetIsFullFragment.isAdded) {
                    widgetIsFullFragment.show(supportFragmentManager, "")
                }
            }

        }, {
            setBorders()
            setButtonVisible()
        })



        setWidgetsTopFragment = SetWidgetsTopFragment({
            binding.topWidgetTvE.text = it.detail
            topWidget = it
        }, {
            setBorders()
            binding.addWidgetLlTextE.visibility = View.VISIBLE
            setButtonVisible()
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvE.visibility = View.GONE
                binding.gifImageViewMirrorE.visibility = View.GONE
                binding.diamondECv.visibility = View.GONE
            } else {

                if (flagHasSeen3Ad == "no"){
                    binding.mirrorCvE.visibility = View.VISIBLE
                    binding.gifImageViewMirrorE.visibility = View.VISIBLE
                    binding.diamondECv.visibility = View.VISIBLE
                }

            }
        })
        setWidgetsTopFragment.isCancelable = false


        binding.clockLlE.setOnClickListener {
            if (editBottomFragment.isAdded == false) {
                binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLlE.setBackgroundResource(R.drawable.icon_circle_little)
                editBottomFragment.show(supportFragmentManager, "")
                binding.addWidgetLlTextE.visibility = View.INVISIBLE
                binding.mirrorCvE.visibility = View.GONE
                binding.gifImageViewMirrorE.visibility = View.GONE
                binding.diamondECv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        binding.cancelCvE.setOnClickListener {
            finish()
        }

        binding.addCvE.setOnClickListener {


            if (MainLockScreenData.getPurchase(this) != "Active") {

                binding.gifImageView222.visibility = View.VISIBLE
                binding.gifImageView222.repeatCount = 280
                binding.gifImageView222.playAnimation()

                binding.dimBackkk.visibility = View.VISIBLE

                RewardedAd.load(
                    this,
                    REWARDED_AD_3,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.i("LOG23", "onAdFailedToLoad: ${adError.message}")

                            mRewardedAd = null
                        }

                        override fun onAdLoaded(rewardedAd: RewardedAd) {

                            mRewardedAd = rewardedAd

                            mRewardedAd?.show(this@EmojiActivity) {

                                fun onUserEarnedReward(rewardItem: RewardItem) {

                                    Toast.makeText(
                                        this@EmojiActivity,
                                        "lock screen add successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val stream = ByteArrayOutputStream()
                                    binding.viewPagerSliderEmojie.drawToBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
                                    val byteArray = stream.toByteArray()

                                    var mutableListString = mutableListOf<String>()
                                    for (widget in widgetSelected) {
                                        mutableListString.add(widget.name)
                                    }
                                     var sctm = System.currentTimeMillis()
                                    mLockScreenViewModel.addLockscreen(
                                        LockScreen(
                                            sctm,
                                            colorSelected!!,
                                            fontSelected!!,
                                            "",
                                            "",
                                            mutableListString,
                                            topWidget.subtitle,
                                            true,
                                            byteArray,
                                            startBackColor
                                        )
                                    )
                                    MainLockScreenData.setSP(
                                        this@EmojiActivity, LockScreen(
                                            sctm,
                                            colorSelected!!,
                                            fontSelected!!,
                                            "",
                                            "",
                                            mutableListString,
                                            topWidget.subtitle,
                                            true,
                                            byteArray,
                                            startBackColor
                                        )
                                    )
                                    MainLockScreenData.setLSRDEmpty(this@EmojiActivity , false)
                                    finish()


                                }
                                Log.i("oaksdoaksd", "onViewCreated: log karddddddddddd")
                                onUserEarnedReward(it)


                            }
                            binding.gifImageView222.visibility = View.INVISIBLE
                            binding.dimBackkk.visibility = View.INVISIBLE
                        }


                    })


            } else {

                Toast.makeText(
                    this@EmojiActivity,
                    "lock screen add successfully",
                    Toast.LENGTH_SHORT
                ).show()

                val stream = ByteArrayOutputStream()
                binding.viewPagerSliderEmojie.drawToBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()


                var mutableListString = mutableListOf<String>()
                for (widget in widgetSelected) {
                    mutableListString.add(widget.name)
                }
                var sctm = System.currentTimeMillis()
                mLockScreenViewModel.addLockscreen(
                    LockScreen(
                        sctm,
                        colorSelected!!,
                        fontSelected!!,
                        "",
                        "",
                        mutableListString,
                        topWidget.subtitle,
                        true,
                        byteArray,
                        startBackColor
                    )
                )
                MainLockScreenData.setSP(
                    this@EmojiActivity, LockScreen(
                        sctm,
                        colorSelected!!,
                        fontSelected!!,
                        "",
                        "",
                        mutableListString,
                        topWidget.subtitle,
                        true,
                        byteArray,
                        startBackColor
                    )
                )
                MainLockScreenData.setLSRDEmpty(this@EmojiActivity , false)
                finish()


            }


        }

        binding.topWidgetLlE.setOnClickListener {
            if (!setWidgetsTopFragment.isAdded) {
                binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLlE.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsTopFragment.show(supportFragmentManager, "")
                binding.addWidgetLlTextE.visibility = View.INVISIBLE
                binding.mirrorCvE.visibility = View.GONE
                binding.gifImageViewMirrorE.visibility = View.GONE
                binding.diamondECv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        setWidgetsBttomFragment.isCancelable = false
        editBottomFragment.isCancelable = false

        binding.widgetsLlE.setOnClickListener {

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                setButtonInvisible()
            }

        }

        adOrPurchaseFragment = AdOrPurchaseFragment("Emoji",1) {

            flagHasSeen3Ad = "yesoooo"

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLlE.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLlE.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                binding.circleIndicatorEmojiSlidero.visibility = View.INVISIBLE
                setButtonInvisible()
                binding.mirrorCvE.visibility = View.GONE
                binding.diamondECv.visibility = View.GONE
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                setClock()
            }
        }

        binding.mirrorCvE.setOnClickListener {
            var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            var eventName = "bottom_widget"
            var eventParams = Bundle().apply {
                putString("param_key", "")
                // Add more parameters if needed
            }
            firebaseAnalytics.logEvent(eventName, eventParams)
            if (!adOrPurchaseFragment.isAdded){
                adOrPurchaseFragment.show(supportFragmentManager, "")
            }

        }

        MainLockScreenData.setEmojiList(this, startEmojies)

        val emojiPagerAdapter = EmojiPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPagerSliderEmojie.adapter = emojiPagerAdapter


        var indicator: CircleIndicator3 = binding.circleIndicatorEmojiSlidero

        indicator.setViewPager( binding.viewPagerSliderEmojie)

        binding.viewPagerSliderEmojie.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                when (position) {
                    0 -> {
                        binding.emojiModelTv.text = "SMALL GRID"
                    }
                    1 -> {
                        binding.emojiModelTv.text = "MEDIUM GRID"
                    }
                    2 -> {
                        binding.emojiModelTv.text = "LARGE GRID"
                    }
                    3 -> {
                        binding.emojiModelTv.text = "RINGS"
                    }
                    4 -> {
                        binding.emojiModelTv.text = "SPIRAL"
                    }
                }
            }



        })

        if (fromEmojiRecycler){
            when(intent.getStringExtra("model")){
                "Small"->{
                    binding.viewPagerSliderEmojie.setCurrentItem(0)
                }
                "Medium"->{
                    binding.viewPagerSliderEmojie.setCurrentItem(1)
                }
                "Large"->{
                    binding.viewPagerSliderEmojie.setCurrentItem(2)
                }
                "Spiral"->{
                    binding.viewPagerSliderEmojie.setCurrentItem(4)
                }
                "Ring"->{
                    binding.viewPagerSliderEmojie.setCurrentItem(3)
                }
            }
        }


        emojiBackgroundFragment = EmojiBackgroundFragment() {

            binding.imageBackooE.setBackgroundColor(Color.parseColor(it))
            startBackColor = it

        }

        emojiBottomSheetFragment = EmojiBottomSheetFragment(startEmojies, {
            startEmojies.add(it)
            MainLockScreenData.setEmojiList(this, startEmojies)
             emojiPagerAdapter.hasChange()
            liveData.postValue(it)
        }, {
            if (startEmojies.size > 0) {
                startEmojies.removeLast()
                MainLockScreenData.setEmojiList(this, startEmojies)
                emojiPagerAdapter.hasChange()
                liveData.postValue("")
            }

        })

        binding.emojiSeBsdBtn.setOnClickListener {
            if (!emojiBottomSheetFragment.isAdded) {
                emojiBottomSheetFragment.show(supportFragmentManager, "")
            }
        }

        binding.emojiBgBsdBtn.setOnClickListener {
            if (!emojiBackgroundFragment.isAdded) {
                emojiBackgroundFragment.show(supportFragmentManager, "")
            }
        }


    }



    fun setClock() {

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)

        binding.clockTvSlse.setText("${myDate}")

    }

    fun setBorders() {
        binding.topWidgetLlE.setBackgroundResource(R.drawable.background_clock)
        binding.clockLlE.setBackgroundResource(R.drawable.background_clock)
        binding.widgetsLlE.setBackgroundResource(R.drawable.background_clock)
    }


    fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat(
            "EEEE, MMMM d"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    fun setButtonInvisible() {
        binding.cancelCvE.visibility = View.INVISIBLE
        binding.addCvE.visibility = View.INVISIBLE
    }

    fun setButtonVisible() {
        binding.cancelCvE.visibility = View.VISIBLE
        binding.addCvE.visibility = View.VISIBLE
    }

    fun setNewDataForWidget() {


        viewModel.WidgetData("Bearer $token", MainLockScreenData.getLocationSP(this))
            .observe(this) {
                Log.i("moooooooooooooooooooooo", "setNewDataForWidget: ${it}")
                try {
                    if (it != null) {

                        if (it!!.success) {
                            MainLockScreenData.setWidgetDataSP(
                                this,
                                WidgetsDataModel(it.success, it.data)
                            )
                        }

                    }
                } catch (e: Exception) {

                }

            }
    }


}