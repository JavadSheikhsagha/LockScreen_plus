package com.a2mp.lockscreen.SetLockScreen

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
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
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import com.a2mp.lockscreen.Api.ViewModel.MainViewModel
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.Home.AdOrPurchaseAddLockScreenFragment
import com.a2mp.lockscreen.Home.AdOrPurchaseFragment
import com.a2mp.lockscreen.Home.REWARDED_AD_2
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.Widgets.SetWidgetsBottomFragment
import com.a2mp.lockscreen.Widgets.SetWidgetsTopFragment
import com.a2mp.lockscreen.Widgets.TopWidgetItems
import com.a2mp.lockscreen.Widgets.WidgetIsFullFragment
import com.a2mp.lockscreen.Widgets.WidgetModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import com.a2mp.lockscreen.Widgets.WidgetsUtility
import com.a2mp.lockscreen.databinding.ActivitySetLockScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SetLockScreenSingleActivity : AppCompatActivity() {

    lateinit var binding: ActivitySetLockScreenBinding
    private val viewModel by viewModels<MainViewModel>()
    lateinit var mLockScreenViewModel: LockScreenViewModel
    var token = "18e16c0f9e0e80bdf0af3f970ef1bbc7aa9352b379a184f0f4eafeb84f92dff7"
    var fetch: Fetch? = null
    lateinit var editBottomFragment: EditBottomFragment
    var urlRemBG = ""
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
    lateinit var adOrPurchaseFragment: AdOrPurchaseFragment
    lateinit var adOrPurchaseAddLockScreenFragment: AdOrPurchaseAddLockScreenFragment
    var flagHasSeen3Ad = "no"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivitySetLockScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClock()
        widgetSelected = mutableListOf()
        var adRequest = AdRequest.Builder().build()

        mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)

        if (MainLockScreenData.getPurchase(this) == "Active") {
            binding.mirrorCvS.visibility = View.GONE
            binding.gifImageViewMirrorS.visibility = View.GONE
            binding.diamondSCv.visibility = View.GONE
        } else {
            binding.gifImageViewMirrorS.visibility = View.VISIBLE
            binding.gifImageViewMirrorS.repeatCount = 280
            binding.gifImageViewMirrorS.playAnimation()
        }

        setNewDataForWidget()

        val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(this)
            .setDownloadConcurrentLimit(3)
            .build()

        fetch = Fetch.getInstance(fetchConfiguration)

        var uri = intent.getStringExtra("uri")
        Log.i("aslfhaofalisfafs", "onCreate: $uri")
        binding.imageBackooS.setImageURI(Uri.parse(uri))


        CoroutineScope(Dispatchers.Main).launch {

            delay(1000)
            val baos = ByteArrayOutputStream()

            try {
                binding.imageBackooS.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
            } catch (e: Exception) {

            }
            val imageBytes: ByteArray = baos.toByteArray()
            val encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            viewModel.RemoveBG("Bearer $token", getRemBG(encodedImage))
                .observe(this@SetLockScreenSingleActivity) {

                    Log.i("asiofnasfmafsom", "onCreate: ${it} ")

                    urlRemBG = it.toString()


                }

        }

        binding.topWidgetTv.text = topWidget.detail

        editBottomFragment = EditBottomFragment({ color, font ->
            binding.clockTvSlss.setTextColor(Color.parseColor(color))
            val typeface = ResourcesCompat.getFont(this, font)
            binding.clockTvSlss.setTypeface(typeface)
            colorSelected = color
            fontSelected = font
        }, {
            val typeface = ResourcesCompat.getFont(this, it)
            binding.clockTvSlss.setTypeface(typeface)
            fontSelected = it
        }, {
            binding.clockTvSlss.setTextColor(Color.parseColor(it))
            colorSelected = it
        }, {
            setBorders()
            binding.addWidgetLlTextS.visibility = View.VISIBLE
            setButtonVisible()
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvS.visibility = View.GONE
                binding.gifImageViewMirrorS.visibility = View.GONE
                binding.diamondSCv.visibility = View.GONE
            } else {

                if (flagHasSeen3Ad == "no") {
                    binding.mirrorCvS.visibility = View.VISIBLE
                    binding.gifImageViewMirrorS.visibility = View.VISIBLE
                    binding.diamondSCv.visibility = View.VISIBLE
                }
            }
        })


        widgetIsFullFragment = WidgetIsFullFragment()


        setWidgetsBttomFragment = SetWidgetsBottomFragment({ wm ->

            if (widgetAdded + wm.sizoo <= 4) {

                widgetAdded += wm.sizoo
                widgetSelected.add(wm)
                binding.addWidgetLlS.visibility = View.GONE

                val view = LayoutInflater.from(binding.widgetsLlS.context)
                    .inflate(wm.view, binding.widgetsLlS, false)
                binding.widgetsLlS.setOnClickListener { null }
                binding.widgetsLlS.isClickable = false
                try {
                    widgetsUtility.setBottomWidgetDetails(view, this, wm.name)
                } catch (e: Exception) {

                }
                binding.widgetsLlS.addView(view)

                view.findViewById<CardView>(R.id.widget_remove_cv).setOnClickListener {
                    binding.widgetsLlS.removeView(view)
                    widgetSelected.remove(wm)
                    widgetAdded -= wm.sizoo
                    Log.i("aslfnqowiroiqwr3324", "onCreate: ${widgetAdded} \t ${wm.sizoo}")
                    if (widgetAdded == 0) {
                        binding.addWidgetLlS.visibility = View.VISIBLE
                        binding.widgetsLlS.setOnClickListener {

                            if (!setWidgetsBttomFragment.isAdded) {
                                setWidgetsBttomFragment.show(supportFragmentManager, "")
                                binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                                binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
                            }

                        }
                    }
                }

                view.setOnClickListener {
                    if (!setWidgetsBttomFragment.isAdded) {
                        setWidgetsBttomFragment.show(supportFragmentManager, "")
                        binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                        binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
                    }
                }

                try {
                    view.findViewById<ImageView>(R.id.click_here)?.setOnClickListener {
                        if (!setWidgetsBttomFragment.isAdded) {
                            setWidgetsBttomFragment.show(supportFragmentManager, "")
                            binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                            binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
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
            binding.topWidgetTv.text = it.detail
            topWidget = it
        }, {
            setBorders()
            binding.addWidgetLlTextS.visibility = View.VISIBLE
            setButtonVisible()
            if (MainLockScreenData.getPurchase(this) == "Active") {
                binding.mirrorCvS.visibility = View.GONE
                binding.gifImageViewMirrorS.visibility = View.GONE
                binding.diamondSCv.visibility = View.GONE
            } else {
                if (flagHasSeen3Ad == "no") {
                    binding.mirrorCvS.visibility = View.VISIBLE
                    binding.gifImageViewMirrorS.visibility = View.VISIBLE
                    binding.diamondSCv.visibility = View.VISIBLE
                }
            }
        })
        setWidgetsTopFragment.isCancelable = false


        binding.clockLlS.setOnClickListener {
            if (editBottomFragment.isAdded == false) {
                binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLlS.setBackgroundResource(R.drawable.icon_circle_little)
                editBottomFragment.show(supportFragmentManager, "")
                binding.addWidgetLlTextS.visibility = View.INVISIBLE
                binding.mirrorCvS.visibility = View.GONE
                binding.gifImageViewMirrorS.visibility = View.GONE
                binding.diamondSCv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        binding.cancelCvS.setOnClickListener {
            finish()
        }

        binding.addCvS.setOnClickListener {
            Log.i("LOG24", "onCreate: onaddClick")

            if (MainLockScreenData.getPurchase(this) != "Active") {
                Log.i("LOG24", "onCreate: isnot active")

                adOrPurchaseAddLockScreenFragment =
                    AdOrPurchaseAddLockScreenFragment("Add Lockscreen", 1) {


                        binding.gifImageView22.visibility = View.VISIBLE
                        binding.gifImageView22.repeatCount = 280
                        binding.gifImageView22.playAnimation()

                        binding.dimBackk.visibility = View.VISIBLE

                        RewardedAd.load(
                            this,
                            REWARDED_AD_2,
                            adRequest,
                            object : RewardedAdLoadCallback() {
                                override fun onAdFailedToLoad(adError: LoadAdError) {
                                    Log.i("LOG23", "onAdFailedToLoad: ${adError.message}")

                                    mRewardedAd = null
                                }

                                override fun onAdLoaded(rewardedAd: RewardedAd) {

                                    mRewardedAd = rewardedAd

                                    mRewardedAd?.show(this@SetLockScreenSingleActivity) {

                                        fun onUserEarnedReward(rewardItem: RewardItem) {

                                            Toast.makeText(
                                                this@SetLockScreenSingleActivity,
                                                "lock screen add successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val filooo =
                                                "/storage/emulated/0/Android/data/com.a2mp.lockscreen/files/Lockscreen" + System.currentTimeMillis() + ".png"

                                            val request = Request(it.toString(), filooo)
                                            request.priority = (Priority.HIGH)
                                            request.networkType = (NetworkType.ALL)


                                            fetch!!.enqueue(request,
                                                { updatedRequest: Request? ->

                                                },
                                                { error: com.tonyodev.fetch2.Error ->

                                                })

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
                                                    uri!!,
                                                    urlRemBG,
                                                    mutableListString,
                                                    topWidget.subtitle,
                                                    false,
                                                    ByteArray(0),
                                                    "#666666"
                                                )
                                            )
                                            MainLockScreenData.setSP(
                                                this@SetLockScreenSingleActivity, LockScreen(
                                                    sctm,
                                                    colorSelected!!,
                                                    fontSelected!!,
                                                    uri!!,
                                                    urlRemBG,
                                                    mutableListString,
                                                    topWidget.subtitle,
                                                    false,
                                                    ByteArray(0),
                                                    "#666666"
                                                )
                                            )
                                            MainLockScreenData.setLSRDEmpty(
                                                this@SetLockScreenSingleActivity,
                                                false
                                            )
                                            finish()


                                        }
                                        Log.i("oaksdoaksd", "onViewCreated: log karddddddddddd")
                                        onUserEarnedReward(it)


                                    }
                                    binding.gifImageView22.visibility = View.INVISIBLE
                                    binding.dimBackk.visibility = View.INVISIBLE
                                }


                            })


                    }


                adOrPurchaseAddLockScreenFragment.show(supportFragmentManager, null)

            } else {
                Log.i("LOG24", "onCreate: toast")

                Toast.makeText(
                    this@SetLockScreenSingleActivity,
                    "lock screen add successfully",
                    Toast.LENGTH_SHORT
                ).show()

                val filooo =
                    "/storage/emulated/0/Android/data/com.a2mp.lockscreen/files/Lockscreen" + System.currentTimeMillis() + ".png"

                val request = Request(it.toString(), filooo)
                request.priority = (Priority.HIGH)
                request.networkType = (NetworkType.ALL)


                fetch!!.enqueue(request,
                    { updatedRequest: Request? ->

                    },
                    { error: com.tonyodev.fetch2.Error ->

                    })

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
                        uri!!,
                        urlRemBG,
                        mutableListString,
                        topWidget.subtitle,
                        false,
                        ByteArray(0),
                        "#666666"
                    )
                )
                MainLockScreenData.setSP(
                    this@SetLockScreenSingleActivity, LockScreen(
                        sctm,
                        colorSelected!!,
                        fontSelected!!,
                        uri!!,
                        urlRemBG,
                        mutableListString,
                        topWidget.subtitle,
                        false,
                        ByteArray(0),
                        "#666666"
                    )
                )
                MainLockScreenData.setLSRDEmpty(this@SetLockScreenSingleActivity, false)
                finish()


            }


        }

        binding.topWidgetLlS.setOnClickListener {
            if (!setWidgetsTopFragment.isAdded) {
                binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
                binding.widgetsLlS.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsTopFragment.show(supportFragmentManager, "")
                binding.addWidgetLlTextS.visibility = View.INVISIBLE
                binding.mirrorCvS.visibility = View.GONE
                binding.gifImageViewMirrorS.visibility = View.GONE
                binding.diamondSCv.visibility = View.GONE
                setButtonInvisible()
            }
        }

        setWidgetsBttomFragment.isCancelable = false
        editBottomFragment.isCancelable = false

        adOrPurchaseFragment = AdOrPurchaseFragment("Add Widgets", 1) {

            flagHasSeen3Ad = "yesoooo"

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                setButtonInvisible()
                binding.mirrorCvS.visibility = View.GONE
                binding.diamondSCv.visibility = View.GONE
            }

        }


        binding.widgetsLlS.setOnClickListener {

            if (!setWidgetsBttomFragment.isAdded) {
                binding.topWidgetLlS.setBackgroundResource(R.drawable.icon_circle_little)
                binding.clockLlS.setBackgroundResource(R.drawable.icon_circle_little)
                setWidgetsBttomFragment.show(supportFragmentManager, "")
                setButtonInvisible()
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                setClock()
            }
        }

        binding.mirrorCvS.setOnClickListener {
            var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            var eventName = "bottom_widget"
            var eventParams = Bundle().apply {
                putString("param_key", "")
                // Add more parameters if needed
            }
            firebaseAnalytics.logEvent(eventName, eventParams)
            if (!adOrPurchaseFragment.isAdded) {
                adOrPurchaseFragment.show(supportFragmentManager, "")
            }

        }

    }

    fun setClock() {

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)

        binding.clockTvSlss.setText("${myDate}")

    }

    fun setBorders() {
        binding.topWidgetLlS.setBackgroundResource(R.drawable.background_clock)
        binding.clockLlS.setBackgroundResource(R.drawable.background_clock)
        binding.widgetsLlS.setBackgroundResource(R.drawable.background_clock)
    }


    private fun getRemBG(base64: String): RequestBody {

        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("url", base64)
            .build()


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
        binding.cancelCvS.visibility = View.INVISIBLE
//        binding.addCvS.visibility = View.INVISIBLE
    }

    fun setButtonVisible() {
        binding.cancelCvS.visibility = View.VISIBLE
        binding.addCvS.visibility = View.VISIBLE
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