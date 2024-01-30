package com.a2mp.lockscreen.SetLockScreen


import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.Api.ViewModel.MainViewModel
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.Home.AdOrPurchaseAddLockScreenFragment
import com.a2mp.lockscreen.Home.AdOrPurchaseFragment
import com.a2mp.lockscreen.Home.PurchaseActivity
import com.a2mp.lockscreen.Home.REWARDED_AD_1
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.Notification.NotificationActivity
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.Widgets.*
import com.a2mp.lockscreen.databinding.ActivitySetLockScreenMultipleBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator3
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception


class SetLockScreenMultipleActivity : AppCompatActivity() {

    lateinit var binding: ActivitySetLockScreenMultipleBinding
    private val viewModel by viewModels<MainViewModel>()
    var token = "278ee94f5d5adbede721cda3e409bb9f35ed68534e5831171a53574e853027d2"
    lateinit var viewPager2: ViewPager2
    var fontSelected: Int? = R.font.roboto_medium
    var colorSelected: String? = "#FFFFFF"
    lateinit var imageBackoo: String
    lateinit var imageFrantoo: String
    lateinit var editBottomFragment: EditBottomFragment
    lateinit var mLockScreenViewModel: LockScreenViewModel
    lateinit var listPictures: MutableList<PictureModel>
    private var mRewardedAd: RewardedAd? = null
    lateinit var setWidgetsBttomFragment: SetWidgetsBottomFragment
    lateinit var widgetIsFullFragment: WidgetIsFullFragment
    var widgetAdded = 0
    lateinit var widgetSelected: MutableList<WidgetModel>
    lateinit var setWidgetsTopFragment: SetWidgetsTopFragment
    var topWidget = TopWidgetItems(0, 0, "${getDate(System.currentTimeMillis())}", "Date")
    var widgetsUtility = WidgetsUtility()
    lateinit var adOrPurchaseFragment: AdOrPurchaseFragment
    lateinit var adOrPurchaseAddLockScreenFragment: AdOrPurchaseAddLockScreenFragment
    var flagHasSeen3Ad = "no"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivitySetLockScreenMultipleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClock()
        widgetSelected = mutableListOf()


        mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)

        if (MainLockScreenData.getPurchase(this) == "Active") {
            binding.mirrorCvM.visibility = View.GONE
            binding.gifImageViewMirrorM.visibility = View.GONE
            binding.diamondMCv.visibility = View.GONE
        } else {
            binding.gifImageViewMirrorM.visibility = View.VISIBLE
            binding.gifImageViewMirrorM.repeatCount = 280
            binding.gifImageViewMirrorM.playAnimation()
        }


        var adRequest = AdRequest.Builder().build()

        var position = intent.getIntExtra("position", 0)
        var category = intent.getStringExtra("category")


        setNewDataForWidget()

        viewModel.GetData("Bearer $token").observe(this) {


            for (item in it.data!!.toMutableList()) {

                if (item.category.name == category) {

                    listPictures = item.pictures.toMutableList()

                    viewPager2 = binding.viewPagerMultiple

                    viewPager2.setAdapter(
                        SetMultipleLockscreenAdapter(
                            item.pictures.toMutableList(),
                            viewPager2
                        ) {


                        })

                    viewPager2.currentItem = position

                    var indicator: CircleIndicator3 = binding.circleIndicator31

                    indicator.setViewPager(viewPager2)


                }

            }

        }

        binding.topWidgetTv.text = topWidget.detail

        editBottomFragment = EditBottomFragment({ color, font ->
            binding.clockTvSlsm.setTextColor(Color.parseColor(color))
            Log.i("aslfnaslfnalsf", "onCreate: ${font}")
            val typeface = ResourcesCompat.getFont(this, font)
            binding.clockTvSlsm.setTypeface(typeface)
            colorSelected = color
            fontSelected = font
        }, {
            val typeface = ResourcesCompat.getFont(this, it)
            binding.clockTvSlsm.setTypeface(typeface)
            fontSelected = it
        }, {
            binding.clockTvSlsm.setTextColor(Color.parseColor(it))
            colorSelected = it
        }, {
            setBorders()
            binding.circleIndicator31.visibility = View.VISIBLE
            if (widgetSelected.size == 0) {
                binding.addWidgetLlText.visibility = View.VISIBLE
            }
            setButtonVisible()
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvM.visibility = View.GONE
                binding.gifImageViewMirrorM.visibility = View.GONE
                binding.diamondMCv.visibility = View.GONE
            } else {

                if (flagHasSeen3Ad == "no") {
                    binding.mirrorCvM.visibility = View.VISIBLE
                    binding.gifImageViewMirrorM.visibility = View.VISIBLE
                    binding.diamondMCv.visibility = View.VISIBLE
                }

            }
        })

        widgetIsFullFragment = WidgetIsFullFragment()


        setWidgetsBttomFragment = SetWidgetsBottomFragment({ wm ->

            if (widgetAdded + wm.sizoo <= 4) {

                widgetAdded += wm.sizoo
                widgetSelected.add(wm)
                binding.addWidgetLl.visibility = View.GONE

                val view = LayoutInflater.from(binding.widgetsLl.context)
                    .inflate(wm.view, binding.widgetsLl, false)
                binding.widgetsLl.setOnClickListener { null }
                binding.widgetsLl.isClickable = false
                try {
                    widgetsUtility.setBottomWidgetDetails(view, this, wm.name)
                } catch (e: Exception) {

                }
                binding.widgetsLl.addView(view)

                view.findViewById<CardView>(R.id.widget_remove_cv).setOnClickListener {
                    binding.widgetsLl.removeView(view)
                    widgetSelected.remove(wm)
                    widgetAdded -= wm.sizoo
                    Log.i("aslfnqowiroiqwr3324", "onCreate: ${widgetAdded} \t ${wm.sizoo}")
                    if (widgetAdded == 0) {
                        binding.addWidgetLl.visibility = View.VISIBLE
                        binding.widgetsLl.setOnClickListener {

                            if (!setWidgetsBttomFragment.isAdded) {
                                setWidgetsBttomFragment.show(supportFragmentManager, "")
                                binding.circleIndicator31.visibility = View.INVISIBLE
                                binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                                binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                            }

                        }
                    }
                }

                view.setOnClickListener {
                    if (!setWidgetsBttomFragment.isAdded) {
                        setWidgetsBttomFragment.show(supportFragmentManager, "")
                        binding.circleIndicator31.visibility = View.INVISIBLE
                        binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                        binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                        setButtonInvisible()
                    }
                }

                try {
                    view.findViewById<ImageView>(R.id.click_here)?.setOnClickListener {
                        if (!setWidgetsBttomFragment.isAdded) {
                            setWidgetsBttomFragment.show(supportFragmentManager, "")
                            binding.circleIndicator31.visibility = View.INVISIBLE
                            binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                            binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                            setButtonInvisible()
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
            binding.circleIndicator31.visibility = View.VISIBLE
            setButtonVisible()
        })



        setWidgetsTopFragment = SetWidgetsTopFragment({
            binding.topWidgetTv.text = it.detail
            topWidget = it
        }, {
            setBorders()
            binding.circleIndicator31.visibility = View.VISIBLE
            binding.addWidgetLlText.visibility = View.VISIBLE
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvM.visibility = View.GONE
                binding.gifImageViewMirrorM.visibility = View.GONE
                binding.diamondMCv.visibility = View.GONE
            } else {
                if (flagHasSeen3Ad == "no") {
                    binding.mirrorCvM.visibility = View.VISIBLE
                    binding.gifImageViewMirrorM.visibility = View.VISIBLE
                    binding.diamondMCv.visibility = View.VISIBLE
                }
            }
            setButtonVisible()
        })
        setWidgetsTopFragment.isCancelable = false


        binding.clockLl.setOnClickListener {
            if (!editBottomFragment.isAdded) {
                binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLl.setBackgroundResource(R.drawable.icon_circle_little)
                editBottomFragment.show(supportFragmentManager, "")
                binding.circleIndicator31.visibility = View.INVISIBLE
                binding.addWidgetLlText.visibility = View.INVISIBLE
                binding.mirrorCvM.visibility = View.GONE
                binding.gifImageViewMirrorM.visibility = View.GONE
                binding.diamondMCv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        binding.cancelCv.setOnClickListener {
            finish()
        }

        binding.addCv.setOnClickListener {

            try {
                Log.i("LOG25", "onCreate:  tap add")
                if (MainLockScreenData.getPurchase(this) != "Active") {
                    Log.i("LOG25", "onCreate:  is not active")
//                    MainLockScreenData.setPurchase(this, "Active")

                    adOrPurchaseAddLockScreenFragment =
                        AdOrPurchaseAddLockScreenFragment("Add Lockscreen", 1) {

                            binding.gifImageView2.visibility = View.VISIBLE
                            binding.gifImageView2.repeatCount = 280
                            binding.gifImageView2.playAnimation()

                            binding.dimBack.visibility = View.VISIBLE

                            RewardedAd.load(
                                this,
                                REWARDED_AD_1,
                                adRequest,
                                object : RewardedAdLoadCallback() {
                                    override fun onAdFailedToLoad(adError: LoadAdError) {
                                        Log.i("LOG25", "onAdFailedToLoad: ${adError.message}")
                                        mRewardedAd = null
                                    }

                                    override fun onAdLoaded(rewardedAd: RewardedAd) {

                                        mRewardedAd = rewardedAd

                                        mRewardedAd?.show(this@SetLockScreenMultipleActivity) {

                                            fun onUserEarnedReward(rewardItem: RewardItem) {
                                                Toast.makeText(
                                                    this@SetLockScreenMultipleActivity,
                                                    "lock screen add successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()

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
                                                        listPictures[viewPager2.currentItem].picName,
                                                        listPictures[viewPager2.currentItem].remBgPicName,
                                                        mutableListString,
                                                        topWidget.subtitle,
                                                        false,
                                                        ByteArray(0),
                                                        ""
                                                    )
                                                )
                                                Log.i(
                                                    "asflanfliafpiqwf",
                                                    "onCreate: 111111111111111111111111111111111111111111"
                                                )
                                                MainLockScreenData.setSP(
                                                    this@SetLockScreenMultipleActivity, LockScreen(
                                                        sctm,
                                                        colorSelected!!,
                                                        fontSelected!!,
                                                        listPictures[viewPager2.currentItem].picName,
                                                        listPictures[viewPager2.currentItem].remBgPicName,
                                                        mutableListString,
                                                        topWidget.subtitle,
                                                        false,
                                                        ByteArray(0),
                                                        ""
                                                    )
                                                )
                                                MainLockScreenData.setLSRDEmpty(
                                                    this@SetLockScreenMultipleActivity,
                                                    false
                                                )
                                                finish()
                                            }
                                            Log.i("oaksdoaksd", "onViewCreated: log karddddddddddd")
                                            onUserEarnedReward(it)

                                        }
                                        binding.gifImageView2.visibility = View.INVISIBLE
                                        binding.dimBack.visibility = View.INVISIBLE
                                    }
                                })


                        }

                    adOrPurchaseAddLockScreenFragment.show(supportFragmentManager, null)

                } else {


                    Toast.makeText(
                        this@SetLockScreenMultipleActivity,
                        "lock screen add successfully",
                        Toast.LENGTH_SHORT
                    ).show()

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
                            listPictures[viewPager2.currentItem].picName,
                            listPictures[viewPager2.currentItem].remBgPicName,
                            mutableListString,
                            topWidget.subtitle,
                            false,
                            ByteArray(0),
                            ""
                        )
                    )
                    Log.i(
                        "asflanfliafpiqwf",
                        "onCreate: 111111111111111111111111111111111111111111"
                    )
                    MainLockScreenData.setSP(
                        this@SetLockScreenMultipleActivity, LockScreen(
                            sctm,
                            colorSelected!!,
                            fontSelected!!,
                            listPictures[viewPager2.currentItem].picName,
                            listPictures[viewPager2.currentItem].remBgPicName,
                            mutableListString,
                            topWidget.subtitle,
                            false,
                            ByteArray(0),
                            ""
                        )
                    )
                    MainLockScreenData.setLSRDEmpty(this@SetLockScreenMultipleActivity, false)
                    finish()

                }


            } catch (e: Exception) {
                Log.i("LOG24", "onCreate: ${e.message}")
            }


        }

        editBottomFragment.isCancelable = false

        binding.dimBack.setOnClickListener {

        }

        binding.topWidgetLl.setOnClickListener {
            if (!setWidgetsTopFragment.isAdded) {
                binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLl.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsTopFragment.show(supportFragmentManager, "")
                binding.circleIndicator31.visibility = View.INVISIBLE
                binding.addWidgetLlText.visibility = View.INVISIBLE
                binding.mirrorCvM.visibility = View.GONE
                binding.gifImageViewMirrorM.visibility = View.GONE
                binding.diamondMCv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        setWidgetsBttomFragment.isCancelable = false

        adOrPurchaseFragment = AdOrPurchaseFragment("Add Widgets",1) {

            flagHasSeen3Ad = "yesoooo"

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                binding.circleIndicator31.visibility = View.INVISIBLE
                setButtonInvisible()
                binding.mirrorCvM.visibility = View.GONE
                binding.diamondMCv.visibility = View.GONE
            }

        }

        binding.widgetsLl.setOnClickListener {

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLl.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLl.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                binding.circleIndicator31.visibility = View.INVISIBLE
                setButtonInvisible()
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                setClock()
            }
        }

        binding.mirrorCvM.setOnClickListener {
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

    }

    fun setClock() {

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)

        binding.clockTvSlsm.setText("${myDate}")

    }

    fun setBorders() {
        binding.topWidgetLl.setBackgroundResource(R.drawable.background_clock)
        binding.clockLl.setBackgroundResource(R.drawable.background_clock)
        binding.widgetsLl.setBackgroundResource(R.drawable.background_clock)
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
        binding.cancelCv.visibility = View.INVISIBLE
        binding.addCv.visibility = View.INVISIBLE
    }

    fun setButtonVisible() {
        binding.cancelCv.visibility = View.VISIBLE
        binding.addCv.visibility = View.VISIBLE
    }

    fun setNewDataForWidget() {


        viewModel.WidgetData("Bearer $token", MainLockScreenData.getLocationSP(this))
            .observe(this) {
                Log.i("moooooooooooooooooooooo", "setNewDataForWidget: ${it}")
                try {
                    if (it!!.success) {
                        MainLockScreenData.setWidgetDataSP(
                            this,
                            WidgetsDataModel(it.success, it.data)
                        )
                    }

                } catch (e: Exception) {

                }
            }


    }

}