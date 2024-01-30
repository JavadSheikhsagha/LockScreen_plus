package com.a2mp.lockscreen.Home


import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Telephony
import android.telecom.TelecomManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Api.ViewModel.MainViewModel
import com.a2mp.lockscreen.Database.LockScreenViewModel
import com.a2mp.lockscreen.Emoji.*
import com.a2mp.lockscreen.LockScreen.LockscreenService
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.MyScreens.MyScreensActivity
import com.a2mp.lockscreen.Notification.LSNotificationListenerService
import com.a2mp.lockscreen.Notification.NotificationActivity
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.SetLockScreen.SetLockScreenMultipleActivity
import com.a2mp.lockscreen.SetLockScreen.SetLockScreenSingleActivity
import com.a2mp.lockscreen.Widgets.LocationModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import com.a2mp.lockscreen.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.permissionx.guolindev.PermissionX
import com.romainpiel.shimmer.Shimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    var token = "278ee94f5d5adbede721cda3e409bb9f35ed68534e5831171a53574e853027d2"
    var keyToGetWallpaperService = "http://157.90.30.203/lockscreen/public/"
    lateinit var listLockScreen: MutableList<ResponseModelLockScreen>
    lateinit var listTopMain: MutableList<TopItemModel>
    lateinit var firstRun: SharedPreferences
    lateinit var mLockScreenViewModel: LockScreenViewModel
    lateinit var locationFragment: LocationFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var counter = 0
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adOrPurchaseFragment: AdOrPurchaseFragment
    lateinit var mEmojiViewModel : EmojiViewModel
    lateinit var spFirstTimeEmoji : SharedPreferences
    lateinit var adOrPurchaseEmojiFragment: AdOrPurchaseEmojiFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTopMain()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        firstRun = getSharedPreferences("firstRun", Context.MODE_PRIVATE)

        Log.i("peiogwnioegnioegn", "onCreate: ${MainLockScreenData.getLocationSP(this)}")

        PermissionX.init(this)
            .permissions(
                Manifest.permission.INTERNET,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                } else {

                }
            }


        val serviceIntent = Intent(this, LockscreenService::class.java)


        Thread {
            ContextCompat.startForegroundService(this, serviceIntent)
        }.start()

        startService(Intent(this, LSNotificationListenerService::class.java))


        binding.moreIv.setOnClickListener {
            showPopup(it)
        }

        listLockScreen = mutableListOf()

        viewModel.GetData("Bearer $token").observe(this) {
            Log.i("TAG1232141412", "onCreate: ${it}")

            try {
                listLockScreen = it.data!!.toMutableList()
                var recycler: RecyclerView = binding.categorysRv
                var adapter = CategorysAdapter(listLockScreen) { it, st ->
                    var intent = Intent(this, SetLockScreenMultipleActivity::class.java)
                    intent.putExtra("position", it)
                    intent.putExtra("category", st)
                    startActivity(intent)
                }
                recycler.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recycler.adapter = adapter

            } catch (e: Exception) {
                Log.i("aslkfmlaskfmkasf", "onCreate: $e")
            }

            var recycler2: RecyclerView = binding.emojiesRv
            var adapter2 = CategoryEmojiAdapter(mutableListOf("Emoji")) { model, emojies, background ->


                if (MainLockScreenData.getPurchase(this) == "Active" || adOrPurchaseEmojiFragment.getAdCount() == 0){
                    var intent = Intent(this, EmojiActivity::class.java)
                    intent.putExtra("emojies", emojies.joinToString(","))
                    intent.putExtra("model", model)
                    intent.putExtra("background", background)
                    intent.putExtra("recycler" , true)
                    startActivity(intent)
                }else{
                    if (!adOrPurchaseEmojiFragment.isAdded) {
                        adOrPurchaseEmojiFragment.show(supportFragmentManager, "")
                    }
                }

                }
            recycler2.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recycler2.adapter = adapter2


        }

        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-5541510796756413/2836139603",
            // TODO: CHANGE!!!
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i("LOG23", "onAdFailedToLoad: ${adError.message}")

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd
                }
            })

        if (firstRun.getString("firstRun", "") != "2828") {

//            CoroutineScope(Dispatchers.Main).launch {
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this@MainActivity)
//                    mInterstitialAd = null
//                } else {
//                    InterstitialAd.load(
//                        this@MainActivity,
//                        "ca-app-pub-6545436330357450/5963531671",
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
//
//                            }
//                        })
//                }
//            }

            var intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)





            firstRun.edit().putString("firstRun", "2828").commit()
        }


        if (System.currentTimeMillis() > MainLockScreenData.getLastTimeGetData(this) + 86400000) {
            setNewDataForWidget()
            MainLockScreenData.setLastTimeGetData(this, System.currentTimeMillis())
        }


        var shimmer = Shimmer()
        shimmer.setRepeatCount(100000)
            .setDuration(1280)
            .setStartDelay(800)
            .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
        shimmer.start(binding.shimmerTv)



        if (MainLockScreenData.getNotifSwitch(this) == 0) {
            binding.notificationTopMainStatus.text = "Off"
        } else {
            binding.notificationTopMainStatus.text = "On"
        }


        adOrPurchaseFragment = AdOrPurchaseFragment("Notification",1) {
            intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
            var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            var eventName = "Card_notification"
            var eventParams = Bundle().apply {
                putString("param_key", "")
                // Add more parameters if needed
            }
            firebaseAnalytics.logEvent(eventName, eventParams)
        }

        adOrPurchaseEmojiFragment = AdOrPurchaseEmojiFragment("Emoji",1)


        binding.notifCard.setOnClickListener {

            if (MainLockScreenData.getPurchase(this) == "Active") {
                intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
            } else {
                if (!adOrPurchaseFragment.isAdded) {
                    adOrPurchaseFragment.show(supportFragmentManager, "")
                }
            }


        }



        spFirstTimeEmoji = getSharedPreferences("spFirstTimeEmoji" , Context.MODE_PRIVATE)


        if (spFirstTimeEmoji.getString("spFirstTimeEmoji" , "notyet") != "done"){

            CoroutineScope(Dispatchers.Default).launch {
                mEmojiViewModel = ViewModelProvider(this@MainActivity).get(EmojiViewModel::class.java)
                var listOfList = mutableListOf<EmojiKeyboardModel>()
                listOfList.add(EmojiKeyboardModel("FREQUENTLY USED", mutableListOf("😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😉", "😊", "😇", "🥰", "🤪", "😎", "😈", "😺", "😸", "😻", "😹", "😼", "😽", "😿", "😾", "🤞", "🤘", "👌", "🤏", "🤙", "👋", "🤚", "👍", "⚡", "🔥", "✨", "💧", "🌊", "☁️", "🌚", "🌝", "🌞", "☄️", "🌟", "⭐", "💫", "❤️", "❤️‍🔥", "💓", "💕", "💯")))
                listOfList.add(EmojiKeyboardModel("SMILEYS & PEOPLE", getEmojiByCategory("smilespeople")))
                listOfList.add(EmojiKeyboardModel("ANIMALS & NATURE", getEmojiByCategory("animals")))
                listOfList.add(EmojiKeyboardModel("FOOD & DRINK", getEmojiByCategory("food")))
                listOfList.add(EmojiKeyboardModel("ACTIVITY", getEmojiByCategory("activity")))
                listOfList.add(EmojiKeyboardModel("TRAVEL & PLACES", getEmojiByCategory("travelplaces")))
                listOfList.add(EmojiKeyboardModel("OBJECTS", getEmojiByCategory("objects")))
                listOfList.add(EmojiKeyboardModel("SYMBOLS", getEmojiByCategory("symbols")))
                listOfList.add(EmojiKeyboardModel("FLAGS", getEmojiByCategory("flags")))
                var coias = 0
                for (list in listOfList){
                    coias++
                    Log.i("asdjhaskudhsauihdasuid", "onCreate: omadddesh$coias")
                    val commaSeparatedString = list.emojis.joinToString(",")
                    mEmojiViewModel.addEmoji(Emoji(id = System.currentTimeMillis().toString() , name = list.title , commaSeparatedString ))

                }

                spFirstTimeEmoji.edit().putString("spFirstTimeEmoji" , "done").apply()
            }

        }


    }

    fun getEmojiByCategory(name: String): MutableList<String> {
        val jsonString =
            this.assets.open("$name.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        val emojiList = parseJsonArray(jsonArray)
        var emojiString: MutableList<String>
        emojiString = mutableListOf()

        for (item in emojiList) {
            emojiString.add("${item.value}")
        }
        return emojiString
    }

    fun parseJsonArray(jsonArray: JSONArray): MutableList<Emoji> {
        val emojiList = mutableListOf<Emoji>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val value = jsonObject.getString("value")
            val emoji = Emoji(System.currentTimeMillis().toString(), name, value)
            emojiList.add(emoji)
        }

        return emojiList
    }


    fun showPopup(v: View?) {
        val popup = PopupMenu(this, v)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.item1 -> {
                    var intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)
                }
                R.id.item2 -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://search?q=pub:Deshawn Straws")
                        )
                    )
                }
                R.id.item3 -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://pages.flycricket.io/ios-16-lock-screen-0/privacy.html")
                    )
                    startActivity(browserIntent)
                }
            }

            true
        })

        popup.inflate(R.menu.popup_menu)
        popup.show()
    }


    fun setupTopMain() {

        listTopMain = mutableListOf()

        listTopMain.add(TopItemModel(R.drawable.icon_myscreens, "your\nwallpaper"))
        listTopMain.add(TopItemModel(R.drawable.icon_photos, "Photos"))
        listTopMain.add(TopItemModel(R.drawable.icon_color, "Color"))
        listTopMain.add(TopItemModel(R.drawable.icon_emoji_top_item, "Emoji"))
        listTopMain.add(TopItemModel(R.drawable.icon_stripe, "Stripe"))
        listTopMain.add(TopItemModel(R.drawable.icon_astoronomy, "Astoronomy"))

        var recycler: RecyclerView = binding.topMainRv
        var adapter = MainTopItemAdapter(listTopMain) {
            when (it) {
                0 -> {
                    var intent = Intent(this, MyScreensActivity::class.java)
                    startActivity(intent)
                    var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
                    var eventName = "your_wallpaper"
                    var eventParams = Bundle().apply {
                        putString("param_key", "")
                        // Add more parameters if needed
                    }
                    firebaseAnalytics.logEvent(eventName, eventParams)
                }
                1 -> {
                    ImagePicker.with(this)
                        .start()
                }
                2 -> {
                    var intent = Intent(this, SetLockScreenMultipleActivity::class.java)
                    intent.putExtra("position", 0)
                    intent.putExtra("category", "Color")
                    startActivity(intent)
                }
                3 -> {
                    if (MainLockScreenData.getPurchase(this) == "Active" || adOrPurchaseEmojiFragment.getAdCount() == 0){
                        var intent = Intent(this, EmojiActivity::class.java)
                        intent.putExtra("position", 0)
                        intent.putExtra("recycler" , false)
                        startActivity(intent)
                    }else{
                        if (!adOrPurchaseEmojiFragment.isAdded) {
                            adOrPurchaseEmojiFragment.show(supportFragmentManager, "")
                        }
                    }
                    var firebaseAnalytics = FirebaseAnalytics.getInstance(this)
                    var eventName = "emoji"
                    var eventParams = Bundle().apply {
                        putString("param_key", "")
                        // Add more parameters if needed
                    }
                    firebaseAnalytics.logEvent(eventName, eventParams)
                }
                4 -> {
                    var intent = Intent(this, SetLockScreenMultipleActivity::class.java)
                    intent.putExtra("position", 0)
                    intent.putExtra("category", "Stripe")
                    startActivity(intent)
                }
                5 -> {
                    var intent = Intent(this, SetLockScreenMultipleActivity::class.java)
                    intent.putExtra("position", 0)
                    intent.putExtra("category", "Astoronomy")
                    startActivity(intent)
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            Log.i("TAGaslfnaslfnlaskf", "onActivityResult: ${uri}")
            var intent = Intent(this, SetLockScreenSingleActivity::class.java)
            intent.putExtra("uri", uri.toString())
            startActivity(intent)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (MainLockScreenData.getNotifSwitch(this) == 0) {
            binding.notificationTopMainStatus.text = "Off"
        } else {
            binding.notificationTopMainStatus.text = "On"
        }

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }

            try {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {
                    var location: Location? = it.result
                    if (location == null) {

                    } else {

                        try {
                            MainLockScreenData.setLocationSP(
                                this,
                                LocationModel(
                                    location.latitude.toString(),
                                    location.longitude.toString(),
                                    System.currentTimeMillis().toString(),
                                    getCountryName(this, location.latitude, location.longitude)!!
                                )
                            )
                        } catch (e: Exception) {

                        }

                    }
                }
            } catch (e: Exception) {

            }

        } catch (e: Exception) {

        }

    }

    fun getLocation() {


        if (checkPermission()) {

            if (isLocationEnabled()) {

                try {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {
                        var location: Location? = it.result
                        if (location == null) {

                        } else {


                            try {
                                MainLockScreenData.setLocationSP(
                                    this,
                                    LocationModel(
                                        location.latitude.toString(),
                                        location.longitude.toString(),
                                        System.currentTimeMillis().toString(),
                                        getCountryName(
                                            this,
                                            location.latitude,
                                            location.longitude
                                        )!!
                                    )
                                )
                            } catch (e: Exception) {

                            }

                        }
                    }
                } catch (e: Exception) {

                }

            } else {

                if (counter == 0) {

                    var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                    counter += 1
                    onPause()
                }


            }

        } else {


            try {
                requestPermissions()

            } catch (e: Exception) {

            }


        }


    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )

    }

    private fun checkPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    }

    fun getCountryName(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context!!, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var result: Address
            return if (addresses != null && !addresses.isEmpty()) {
                addresses[0].getCountryName()
            } else null
        } catch (ignored: IOException) {
            //do something
        }
        return addresses?.get(0)?.getCountryName()
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }

    }

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    fun setNewDataForWidget() {


        viewModel.WidgetData("Bearer $token", MainLockScreenData.getLocationSP(this))
            .observe(this) {
                Log.i("moooooooooooooooooooooo", "setNewDataForWidget: ${it}")
                if (it != null) {
                    MainLockScreenData.setWidgetDataSP(this, WidgetsDataModel(it.success, it.data))
                }

            }

    }


}