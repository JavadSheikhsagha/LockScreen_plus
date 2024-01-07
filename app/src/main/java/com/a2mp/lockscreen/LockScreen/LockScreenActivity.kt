package com.a2mp.lockscreen.LockScreen


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.ViewModel.MainViewModel
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.Notification.*
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import com.a2mp.lockscreen.Widgets.WidgetsUtility
import com.a2mp.lockscreen.databinding.ActivityLockScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class LockScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityLockScreenBinding
    lateinit var mLockScreenViewModel: LockScreenViewModel
    var isFlash = false
    private lateinit var cameraM: CameraManager
    lateinit var firstLock: SharedPreferences
    private val viewModel by viewModels<MainViewModel>()
    var token = "278ee94f5d5adbede721cda3e409bb9f35ed68534e5831171a53574e853027d2"
    var widgetsUtility = WidgetsUtility()
    private var mInterstitialAd: InterstitialAd? = null
    var adRequest = AdRequest.Builder().build()
    lateinit var mNotificationViewModel: NotificationViewModel
    lateinit var listNotifications: MutableList<NotificationStackModel>
    var counter = 0
    var listNotifSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLockScreenBinding.inflate(layoutInflater)
        if (!MainLockScreenData.getCloseStatus(this)) {
            finish()
        }
        setContentView(binding.root)

        fullScreen(this)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )
        setClock()

        firstLock = getSharedPreferences("firstLock", Context.MODE_PRIVATE)


        setNewDataForWidget()


        InterstitialAd.load(
            this,
            "ca-app-pub-6545436330357450/2407148865",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd
                }
            })


        if (firstLock.getBoolean("firstLock", true) == true) {

            mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)
            mLockScreenViewModel.readAllData.observe(this) {

                try {
                    MainLockScreenData.setSP(this, it[0])
                } catch (e: Exception) {

                }
                Log.i("ashfasbfkabskbjsaf", "onCreate: asfhbaskfjbaskfbjaskfjkasjkbfask2")
                setupClockAndWidget(it.toMutableList())

            }
            firstLock.edit().putBoolean("firstLock", false).commit()

        } else {

            mLockScreenViewModel = ViewModelProvider(this).get(LockScreenViewModel::class.java)
            mLockScreenViewModel.readAllData.observe(this) {



                setupClockAndWidget(it.toMutableList())
                Log.i("ashfasbfkabskbjsaf", "onCreate: asfhbaskfjbaskfbjaskfjkasjkbfask1")

                }

        }



        binding.swapIv.setOnTouchListener(OnSwipeTouchListener(this, {
            finish()
        }, {

        }))


        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                setClock()
            }
        }





        binding.viewPagerSlider.setOnLongClickListener { // TODO Auto-generated method stub if (flag){

            var activityOptionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                binding.viewPagerSlider,
                "driveitgototow"
            )
            var intent = Intent(this, ChangeScreenActivity::class.java)
            intent.putExtra("id", MainLockScreenData.getSP(this).id)
            startActivity(intent, activityOptionCompat.toBundle())
            CoroutineScope(Dispatchers.Main).launch {
                delay(700)
                finish()
                this.cancel()
            }


            true
        }

        binding.cameraCv.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 7)
        }

        cameraM = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        binding.flashlightCv.setOnClickListener {
            flashLightOnRoOff(it)
        }



        if (MainLockScreenData.getNotifSwitch(this) == 1) {
            mNotificationViewModel =
                ViewModelProvider(this@LockScreenActivity).get(NotificationViewModel::class.java)


            listNotifications = mutableListOf()
            var recycler: RecyclerView = binding.notifRvMainLock
            var adapter = NotificationAdapter(this, listNotifications, {

                mNotificationViewModel.deleteNotification(it)

            }, { it, size ->

                mNotificationViewModel.deleteAllByPackageName(it.packageName)
                Log.i("aslfnaslfalsfjaslfkas", "onCreate: $size")
                if (size == 0 && MainLockScreenData.getNotifModel(this) == 0) {
                    binding.containerMotionLsRv.transitionToStart()
                }
            })
            recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler.adapter = adapter


            mNotificationViewModel.readAllData.observe(this@LockScreenActivity) {

                CoroutineScope(Dispatchers.IO).launch {

                    runOnUiThread {
                        binding.notificationCountTv.text = "${it.size} Notifications"

                        if (MainLockScreenData.getNotifModel(this@LockScreenActivity) == 1 && it.isNotEmpty()) {
                            binding.notificationCountLl.visibility = View.GONE
                            binding.containerMotionLsRv.transitionToEnd()
                        }

                    }


                    var listJadidNotif: MutableList<NotificationStackModel>
                    listJadidNotif = mutableListOf()

                    for (item in mNotificationViewModel.getUniquePackageName()) {

                        var appInfooo = getAppNameAndIcon(item)

                        listJadidNotif.add(
                            (NotificationStackModel(
                                item,
                                mNotificationViewModel.getNotificationByPackageName(item)
                                    .asReversed(),
                                appInfooo.name,
                                appInfooo.icon
                            )
                                    )
                        )

                    }


                    recycler.post {
                        adapter.setItems(listJadidNotif.asReversed())
                    }

                    listNotifSize = it.size


                }

            }
            binding.notificationCountLl.setOnTouchListener(
                OnSwipeTouchListener(
                    this@LockScreenActivity,
                    {

                        if (counter == 0 && listNotifSize != 0) {
                            binding.containerMotionLsRv.transitionToEnd()
                            counter++
                        }


                    },
                    {

                    })
            )




            binding.clearAllCvAll.setOnClickListener {
                mNotificationViewModel.deleteAllNotification()
                adapter.setItems(listNotifications)
                binding.containerMotionLsRv.transitionToStart()
                counter = 0
            }

        } else {
            binding.notificationCountLl.visibility = View.GONE
        }


    }

    fun getAppNameAndIcon(packageName: String): AppInfoModel {

        when (packageName) {
            "com.instagram.android" -> {
                return AppInfoModel("Instagram", R.drawable.img_icon_instagram)
            }
            "com.facebook.lite", "com.facebook.katana" -> {
                return AppInfoModel("Facebook", R.drawable.img_icon_facebook)
            }
            "com.linkedin.android" -> {
                return AppInfoModel("LinkedIn", R.drawable.img_icon_linkedin)
            }
            "com.twitter.android" -> {
                return AppInfoModel("Twitter", R.drawable.img_icon_twitter)
            }
            "com.snapchat.android" -> {
                return AppInfoModel("Snapchat", R.drawable.img_icon_snapchat)
            }
            "org.telegram.messenger", "org.thunderdog.challegram" -> {
                return AppInfoModel("Telegram", R.drawable.img_icon_telegram)
            }
            "com.zhiliaoapp.musically" -> {
                return AppInfoModel("TikTok", R.drawable.img_icon_tiktok)
            }
            "com.whatsapp", "com.whatsapp.w4b" -> {
                return AppInfoModel("WhatsApp", R.drawable.img_icon_whatsapp)
            }
            "com.tencent.mm" -> {
                return AppInfoModel("WeChat", R.drawable.img_icon_wechat)
            }
            "com.discord" -> {
                return AppInfoModel("Discord", R.drawable.img_icon_discord)
            }
            "com.spotify.music" -> {
                return AppInfoModel("Spotify", R.drawable.img_icon_spotify)
            }
            "com.google.android.youtube" -> {
                return AppInfoModel("YouTube", R.drawable.img_icon_youtube)
            }
            "com.soundcloud.android" -> {
                return AppInfoModel("SoundCloud", R.drawable.img_icon_soundcloud)
            }
            "com.pinterest" -> {
                return AppInfoModel("Pinterest", R.drawable.img_icon_pinterest)
            }
            "com.tinder", "com.tinder.tinderlite" -> {
                return AppInfoModel("Tinder", R.drawable.img_icon_tinder)
            }
            "tv.twitch.android.app" -> {
                return AppInfoModel("Twitch", R.drawable.img_icon_twitch)
            }
            "com.google.android.gm" -> {
                return AppInfoModel("Gmail", R.drawable.img_icon_gmail)
            }
            "com.android.chrome" -> {
                return AppInfoModel("Google Chrome", R.drawable.img_icon_chrome)
            }
            "com.facebook.orca" -> {
                return AppInfoModel("Messenger", R.drawable.img_icon_messenger)
            }
            "com.google.android.googlequicksearchbox" -> {
                return AppInfoModel("Google", R.drawable.img_icon_google)
            }
            "com.samsung.android.dialer", "com.google.android.dialer" -> {
                return AppInfoModel("Phone", R.drawable.img_icon_call)
            }
            "com.google.android.apps.messaging", "com.messaging.sms" -> {
                return AppInfoModel("Messages", R.drawable.img_icon_message)
            }
            else -> {
                return AppInfoModel("not found", R.drawable.icon_circle_little)
            }
        }

    }

    private fun flashLightOnRoOff(v: View?) {
        /**set flash code*/
        if (!isFlash) {
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId, true)
            isFlash = true

        } else {
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId, false)
            isFlash = false
        }

    }


    fun setClock() {

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)

        binding.clockTvLs.setText("${myDate}")

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

    fun setupClockAndWidget(listooo : MutableList<LockScreen>) {

        var ls = MainLockScreenData.getSP(this)
        if (listooo.isEmpty()){
            finish()
        }else{
            var counter = 0
            for (lockScreen in listooo){

                if (ls == lockScreen){
                    ls = MainLockScreenData.getSP(this)
                    break
                }

                if (counter == listooo.size){
                    ls = listooo[0]
                    MainLockScreenData.setSP(this,ls)
                }
                counter++
            }


            try {
                val typeface = ResourcesCompat.getFont(this, ls.font)
                binding.clockTvLs.setTypeface(typeface)
            } catch (e: Exception) {

            }

            Log.i("yasofnoiafsfas", "onCreate: ${ls.imageFront} \n ${ls.imageBack}")

            if (ls.isEmoje == true) {
                binding.imageEmojiLockscreen.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        ls.emoji,
                        0,
                        ls.emoji!!.size
                    )
                )
                binding.imageBackLockscreen.setBackgroundColor(Color.parseColor(ls.backColor!!))
            } else {
                if (ls.imageBack.contains("/")) {
                    binding.imageFrontLockscreen.setImageURI(Uri.parse(ls.imageFront))
                    binding.imageBackLockscreen.setImageURI(Uri.parse(ls.imageBack))
                } else {
                    Picasso.get()
                        .load("http://157.90.30.203/lockscreen/public/${ls.imageFront}")
                        .into(binding.imageFrontLockscreen)

                    Picasso.get()
                        .load("http://157.90.30.203/lockscreen/public/${ls.imageBack}")
                        .into(binding.imageBackLockscreen)
                }
            }

            binding.clockTvLs.setTextColor(Color.parseColor(ls.color))


            Log.i("asfinoinqw", "onCreate: ${ls.bottomWidgets}")
            for (widget in ls.bottomWidgets) {

                if (widget != "null") {
                    val view = LayoutInflater.from(binding.widgetsLlMainLock.context)
                        .inflate(
                            widgetsUtility.returnViewFromName(widget),
                            binding.widgetsLlMainLock,
                            false
                        )
                    view.findViewById<CardView>(R.id.widget_remove_cv).visibility = View.GONE
                    view.findViewById<CardView>(R.id.widget_back)
                        .setBackgroundResource(R.drawable.icon_circle_little)

                    try {
                        view.findViewById<ImageView>(R.id.click_here).setOnClickListener { }
                        view.findViewById<ImageView>(R.id.click_here).isClickable = true
                    } catch (e: Exception) {
                    }

                    binding.widgetsLlMainLock.addView(view)

                    widgetsUtility.setBottomWidgetDetails(view, this, widget)


                }

                binding.topWidgetTvMainLock.text =
                    widgetsUtility.setTopWidgetDetails(this, ls.topWidget)


            }



        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("zsryexcyvublhnjibhuvtiycrd", "onBackPressed: omad back")
        MainLockScreenData.setCloseStatus(this, true, "back")

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("zsryexcyvublhnjibhuvtiycrd", "onBackPressed: omad des")
        MainLockScreenData.setCloseStatus(this, true, "destroy")

    }

    override fun finish() {
        super.finish()
        Log.i("zsryexcyvublhnjibhuvtiycrd", "onBackPressed: omad fin")
        MainLockScreenData.setCloseStatus(this, true, "finish")

    }

    override fun onStop() {
        super.onStop()

        MainLockScreenData.setCloseStatus(this, false, "stop")

    }

    override fun onStart() {
        super.onStart()

        MainLockScreenData.setCloseStatus(this, false, "start")

    }

    override fun onResume() {
        super.onResume()

//        if (MainLockScreenData.getPurchase(this) != "Active") {
//
//            if (MainLockScreenData.getMainLockAd(this) >= 20) {
//
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this)
//                    mInterstitialAd = null
//                } else {
//                    InterstitialAd.load(
//                        this,
//                        "ca-app-pub-6545436330357450/9076780172",
//                        adRequest,
//                        object : InterstitialAdLoadCallback() {
//                            override fun onAdFailedToLoad(adError: LoadAdError) {
//
//                                mInterstitialAd = null
//                            }
//
//                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//
//                                mInterstitialAd = interstitialAd
//                                MainLockScreenData.setMainLockAd(this@LockScreenActivity, 0)
//                            }
//                        })
//                }
//
//            } else {
//                MainLockScreenData.setMainLockAd(
//                    this@LockScreenActivity,
//                    MainLockScreenData.getMainLockAd(this@LockScreenActivity) + 1
//                )
//            }
//        }
        MainLockScreenData.setCloseStatus(this, false, "resume")

    }

    fun fullScreen(activity: Activity?) {
        if (activity == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            val windowInsetsController =
                ViewCompat.getWindowInsetsController(activity.window.decorView) ?: return
            activity.window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            Log.i("FullScreen", "fullScreen: ")
            activity.window.navigationBarColor = activity.getColor(R.color.white)
            windowInsetsController.isAppearanceLightStatusBars = true
            // Configure the behavior of the hidden system bars
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        } else {
            val window = activity.window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            window.decorView.systemUiVisibility = uiOptions
        }

    }


}