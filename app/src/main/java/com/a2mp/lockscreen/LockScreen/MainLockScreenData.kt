package com.a2mp.lockscreen.LockScreen

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.Widgets.LocationModel
import com.a2mp.lockscreen.Widgets.MoonData
import com.a2mp.lockscreen.Widgets.ResponseDataModel
import com.a2mp.lockscreen.Widgets.WidgetsDataModel
import java.text.SimpleDateFormat
import java.util.*

object MainLockScreenData {

    fun setEmojiList(context: Context , listooo: MutableList<String>){
        val sharedPreferences = context.getSharedPreferences("emoji_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val commaSeparatedString = listooo.joinToString(",")
        editor.putString("emoji_prefs", commaSeparatedString)
        editor.apply()
    }

    fun getEmojiList(context: Context) : MutableList<String>?{
        val sharedPreferences = context.getSharedPreferences("emoji_prefs", Context.MODE_PRIVATE)
        val commaSeparatedString = sharedPreferences.getString("emoji_prefs", null)

        val yourMutableList = commaSeparatedString?.split(",")?.toMutableList()

        return yourMutableList
    }

    fun setLocationSP(context: Context, location: LocationModel) {

        var locationSP: SharedPreferences =
            context.getSharedPreferences("locationSP", Context.MODE_PRIVATE)
        locationSP.edit().putString("latSP", location.lat).commit()
        locationSP.edit().putString("lonSP", location.lon).commit()
        locationSP.edit().putString("dateSP", location.date).commit()
        locationSP.edit().putString("timeZoneSP", location.timeZone).commit()

    }

    fun getLocationSP(context: Context): LocationModel {

        var locationSP: SharedPreferences =
            context.getSharedPreferences("locationSP", Context.MODE_PRIVATE)

        return LocationModel(
            lat = locationSP.getString("latSP", "51.5072")!!,
            lon = locationSP.getString("lonSP", "0.1276")!!,
            date = locationSP.getString("dateSP", "${System.currentTimeMillis()}")!!,
            timeZone = locationSP.getString("timeZoneSP", "london")!!
        )
    }

    fun setSP(context: Context, Model: LockScreen) {

        var idSP: SharedPreferences = context.getSharedPreferences("idSP", Context.MODE_PRIVATE)
        var imageFrontSP: SharedPreferences =
            context.getSharedPreferences("imageFrontSP", Context.MODE_PRIVATE)
        var imageBackSP: SharedPreferences =
            context.getSharedPreferences("imageBackSP", Context.MODE_PRIVATE)
        var fontSP: SharedPreferences = context.getSharedPreferences("fontSP", Context.MODE_PRIVATE)
        var colorSP: SharedPreferences =
            context.getSharedPreferences("colorSP", Context.MODE_PRIVATE)
        var bottomWidgetOneSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetOneSP", Context.MODE_PRIVATE)
        var bottomWidgetTwoSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetTwoSP", Context.MODE_PRIVATE)
        var bottomWidgetThreeSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetThreeSP", Context.MODE_PRIVATE)
        var bottomWidgetFourSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetFourSP", Context.MODE_PRIVATE)
        var topWidgetSP: SharedPreferences =
            context.getSharedPreferences("topWidgetSP", Context.MODE_PRIVATE)
        var isEmojie: SharedPreferences =
            context.getSharedPreferences("isEmojie", Context.MODE_PRIVATE)
        var emoji: SharedPreferences =
            context.getSharedPreferences("emoji", Context.MODE_PRIVATE)
        var backColor: SharedPreferences =
            context.getSharedPreferences("backColor", Context.MODE_PRIVATE)

        idSP.edit().putLong("idSP", Model.id).apply()
        imageFrontSP.edit().putString("imageFrontSP", Model.imageFront).apply()
        imageBackSP.edit().putString("imageBackSP", Model.imageBack).apply()
        fontSP.edit().putInt("fontSP", Model.font).apply()
        colorSP.edit().putString("colorSP", Model.color).apply()
        when(Model.bottomWidgets.size){
            1 ->{
                bottomWidgetOneSP.edit().putString("bottomWidgetOneSP", Model.bottomWidgets[0]).apply()
                bottomWidgetTwoSP.edit().putString("bottomWidgetTwoSP", "null").apply()
                bottomWidgetThreeSP.edit().putString("bottomWidgetThreeSP", "null").apply()
                bottomWidgetFourSP.edit().putString("bottomWidgetFourSP", "null").apply()
            }
            2 ->{
                bottomWidgetOneSP.edit().putString("bottomWidgetOneSP", Model.bottomWidgets[0]).apply()
                bottomWidgetTwoSP.edit().putString("bottomWidgetTwoSP", Model.bottomWidgets[1]).apply()
                bottomWidgetThreeSP.edit().putString("bottomWidgetThreeSP", "null").apply()
                bottomWidgetFourSP.edit().putString("bottomWidgetFourSP", "null").apply()
            }
            3 ->{
                bottomWidgetOneSP.edit().putString("bottomWidgetOneSP", Model.bottomWidgets[0]).apply()
                bottomWidgetTwoSP.edit().putString("bottomWidgetTwoSP", Model.bottomWidgets[1]).apply()
                bottomWidgetThreeSP.edit().putString("bottomWidgetThreeSP", Model.bottomWidgets[2]).apply()
                bottomWidgetFourSP.edit().putString("bottomWidgetFourSP", "null").apply()
            }
            4 ->{
                bottomWidgetOneSP.edit().putString("bottomWidgetOneSP", Model.bottomWidgets[0]).apply()
                bottomWidgetTwoSP.edit().putString("bottomWidgetTwoSP", Model.bottomWidgets[1]).apply()
                bottomWidgetThreeSP.edit().putString("bottomWidgetThreeSP", Model.bottomWidgets[2]).apply()
                bottomWidgetFourSP.edit().putString("bottomWidgetFourSP", Model.bottomWidgets[3]).apply()
            }
            else -> {

            }
        }
        topWidgetSP.edit().putString("topWidgetSP", Model.topWidget).apply()

        isEmojie.edit().putBoolean("isEmojie",Model.isEmoje == true).apply()
        var encodedString: String = android.util.Base64.encodeToString(Model.emoji,android.util.Base64.DEFAULT)
        emoji.edit().putString("emoji",encodedString).apply()
        backColor.edit().putString("backColor",Model.backColor).apply()



    }

    fun getSP(context: Context): LockScreen {

        var idSP: SharedPreferences = context.getSharedPreferences("idSP", Context.MODE_PRIVATE)
        var imageFrontSP: SharedPreferences =
            context.getSharedPreferences("imageFrontSP", Context.MODE_PRIVATE)
        var imageBackSP: SharedPreferences =
            context.getSharedPreferences("imageBackSP", Context.MODE_PRIVATE)
        var fontSP: SharedPreferences = context.getSharedPreferences("fontSP", Context.MODE_PRIVATE)
        var colorSP: SharedPreferences =
            context.getSharedPreferences("colorSP", Context.MODE_PRIVATE)
        var bottomWidgetOneSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetOneSP", Context.MODE_PRIVATE)
        var bottomWidgetTwoSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetTwoSP", Context.MODE_PRIVATE)
        var bottomWidgetThreeSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetThreeSP", Context.MODE_PRIVATE)
        var bottomWidgetFourSP: SharedPreferences =
            context.getSharedPreferences("bottomWidgetFourSP", Context.MODE_PRIVATE)
        var topWidgetSP: SharedPreferences =
            context.getSharedPreferences("topWidgetSP", Context.MODE_PRIVATE)

        var mutableListBottomWidget = mutableListOf<String>(
            bottomWidgetOneSP.getString("bottomWidgetOneSP", "null")!!,
            bottomWidgetTwoSP.getString("bottomWidgetTwoSP", "null")!!,
            bottomWidgetThreeSP.getString("bottomWidgetThreeSP", "null")!!,
            bottomWidgetFourSP.getString("bottomWidgetFourSP", "null")!!
        )

        var isEmojie: SharedPreferences =
            context.getSharedPreferences("isEmojie", Context.MODE_PRIVATE)
        var emoji: SharedPreferences =
            context.getSharedPreferences("emoji", Context.MODE_PRIVATE)
        var backColor: SharedPreferences =
            context.getSharedPreferences("backColor", Context.MODE_PRIVATE)

        val formatter = SimpleDateFormat(
            "EEEE, MMMM dd"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())



        return LockScreen(
            id = idSP.getLong("idSP", 0L),
            imageFront = imageFrontSP.getString("imageFrontSP", "")!!,
            imageBack = imageBackSP.getString("imageBackSP", "")!!,
            font = fontSP.getInt("fontSP", R.font.roboto_medium),
            color = colorSP.getString("colorSP", "#FFFFFF")!!,
            bottomWidgets = mutableListBottomWidget,
            topWidget = topWidgetSP.getString("topWidgetSP", "${formatter.format(calendar.getTime())}")!!,
            isEmoje = isEmojie.getBoolean("isEmojie",false),
            emoji = android.util.Base64.decode(emoji.getString("emoji",""), android.util.Base64.DEFAULT),
            backColor = backColor.getString("backColor","")!!
        )

    }

    fun setWidgetDataSP(context: Context, WidgetsData: WidgetsDataModel) {

        var widgetsDataModelSP: SharedPreferences =
            context.getSharedPreferences("WidgetsDataModelSP", Context.MODE_PRIVATE)
        widgetsDataModelSP.edit().putInt("temperatureSP", WidgetsData.data.temperature).commit()
        widgetsDataModelSP.edit().putString("airConditionSP", WidgetsData.data.airCondition)
            .commit()
        widgetsDataModelSP.edit().putInt("windDirectionSP", WidgetsData.data.windDirection).commit()
        widgetsDataModelSP.edit().putInt("windSpeedSP", WidgetsData.data.windSpeed).commit()
        widgetsDataModelSP.edit().putInt("precipitationSP", WidgetsData.data.precipitation).commit()
        widgetsDataModelSP.edit().putString("sunsetSP", WidgetsData.data.sunset).commit()
        widgetsDataModelSP.edit().putString("sunriseSP", WidgetsData.data.sunrise).commit()
        widgetsDataModelSP.edit()
            .putInt("temperature_2m_minSP", WidgetsData.data.temperature_2m_min).commit()
        widgetsDataModelSP.edit()
            .putInt("temperature_2m_maxSP", WidgetsData.data.temperature_2m_max).commit()
        widgetsDataModelSP.edit().putInt("europeanAqiSP", WidgetsData.data.europeanAqi).commit()
        widgetsDataModelSP.edit().putInt("uvIndexSP", WidgetsData.data.uvIndex).commit()
        widgetsDataModelSP.edit().putString("moonSP", WidgetsData.data.moonPhase.name).commit()
        widgetsDataModelSP.edit().putString("moonriseSP", WidgetsData.data.moonPhase.name).commit()
        widgetsDataModelSP.edit().putString("moonsetSP", WidgetsData.data.moonPhase.name).commit()


    }

    fun getWidgetDataSP(context: Context): WidgetsDataModel {

        var widgetsDataModelSP: SharedPreferences =
            context.getSharedPreferences("WidgetsDataModelSP", Context.MODE_PRIVATE)

        return WidgetsDataModel(
            true,
            ResponseDataModel(
                widgetsDataModelSP.getInt("temperatureSP", 0),
                widgetsDataModelSP.getString("airConditionSP", "")!!,
                widgetsDataModelSP.getInt("windDirectionSP", 0),
                widgetsDataModelSP.getInt("windSpeedSP", 0),
                MoonData(widgetsDataModelSP.getString("moonSP", "")!!, "", ""),
                widgetsDataModelSP.getInt("precipitationSP", 0),
                widgetsDataModelSP.getString("sunsetSP", "")!!,
                widgetsDataModelSP.getString("sunriseSP", "")!!,
                widgetsDataModelSP.getInt("temperature_2m_minSP", 0),
                widgetsDataModelSP.getInt("temperature_2m_maxSP", 0),
                widgetsDataModelSP.getInt("europeanAqiSP", 0),
                widgetsDataModelSP.getInt("uvIndexSP", 0),
                widgetsDataModelSP.getString("moonriseSP", "")!!,
                widgetsDataModelSP.getString("moonsetSP", "")!!
            )
        )
    }

    fun setLastTimeGetData(context: Context, currentTime: Long) {

        var lastTimeGetDataSP: SharedPreferences =
            context.getSharedPreferences("lastTimeGetDataSP", Context.MODE_PRIVATE)

        lastTimeGetDataSP.edit().putLong("lastTimeGetDataSP", currentTime).commit()

    }

    fun getLastTimeGetData(context: Context): Long {

        var lastTimeGetDataSP: SharedPreferences =
            context.getSharedPreferences("lastTimeGetDataSP", Context.MODE_PRIVATE)


        return lastTimeGetDataSP.getLong("lastTimeGetDataSP", 0L)
    }

    fun setOnBoard(context: Context, boolean : Boolean) {

        var onBoardSp: SharedPreferences =
            context.getSharedPreferences("onBoardSp", Context.MODE_PRIVATE)

        onBoardSp.edit().putBoolean("onBoardSp", boolean).commit()

    }

    fun getOnBoard(context: Context): Boolean {

        var onBoardSp: SharedPreferences =
            context.getSharedPreferences("onBoardSp", Context.MODE_PRIVATE)


        return onBoardSp.getBoolean("onBoardSp", false)
    }

    fun setLSRDEmpty(context: Context, boolean : Boolean ) {

        var LSRDEmptySp: SharedPreferences =
            context.getSharedPreferences("LSRDEmptySp", Context.MODE_PRIVATE)

            LSRDEmptySp.edit().putBoolean("LSRDEmptySp", boolean).commit()

    }

    fun getLSRDEmpty(context: Context): Boolean {

        var LSRDEmptySp: SharedPreferences =
            context.getSharedPreferences("LSRDEmptySp", Context.MODE_PRIVATE)

        return LSRDEmptySp.getBoolean("LSRDEmptySp", true)
    }

    fun setCloseStatus(context: Context, boolean : Boolean , where : String) {

        var closeStatusSp: SharedPreferences =
            context.getSharedPreferences("closeStatusSp", Context.MODE_PRIVATE)

        Log.i("zsryexcyvublhnjibhuvtiycrd", "setCloseStatus: ${boolean}\t${where}")
        if (where == "service"){
            closeStatusSp.edit().putBoolean("closeStatusSp", false).commit()
        }else{
            closeStatusSp.edit().putBoolean("closeStatusSp", boolean).commit()
        }


    }

    fun getCloseStatus(context: Context): Boolean {

        var closeStatusSp: SharedPreferences =
            context.getSharedPreferences("closeStatusSp", Context.MODE_PRIVATE)


        return closeStatusSp.getBoolean("closeStatusSp", true)
    }

    fun setPurchase(context: Context, Active : String) {

        var PurchaseSP: SharedPreferences =
            context.getSharedPreferences("PurchaseSP", Context.MODE_PRIVATE)

        PurchaseSP.edit().putString("PurchaseSP", Active).commit()

    }

    fun getPurchase(context: Context): String? {

        var PurchaseSP: SharedPreferences =
            context.getSharedPreferences("PurchaseSP", Context.MODE_PRIVATE)


        return PurchaseSP.getString("PurchaseSP", "NotActive")
    }


    fun setMainLockAd(context: Context, Active : Int) {

        var MainLockAdSP: SharedPreferences =
            context.getSharedPreferences("MainLockAdSP", Context.MODE_PRIVATE)

        MainLockAdSP.edit().putInt("MainLockAdSP", Active).commit()

    }

    fun getMainLockAd(context: Context): Int {

        var MainLockAdSP: SharedPreferences =
            context.getSharedPreferences("MainLockAdSP", Context.MODE_PRIVATE)


        return MainLockAdSP.getInt("MainLockAdSP", 0)
    }


    fun setNotifSwitch(context: Context, switch : Int) {

        var NotifSwitchSP: SharedPreferences =
            context.getSharedPreferences("NotifSwitchSP", Context.MODE_PRIVATE)

        NotifSwitchSP.edit().putInt("NotifSwitchSP", switch).commit()

    }

    fun getNotifSwitch(context: Context): Int {

        var NotifSwitchSP: SharedPreferences =
            context.getSharedPreferences("NotifSwitchSP", Context.MODE_PRIVATE)


        return NotifSwitchSP.getInt("NotifSwitchSP", 0)
    }

    fun setNotifSC(context: Context, switch : Int) {

        var NotifSCSP: SharedPreferences =
            context.getSharedPreferences("NotifSCSP", Context.MODE_PRIVATE)

        NotifSCSP.edit().putInt("NotifSCSP", switch).commit()

    }

    fun getNotifSC(context: Context): Int {

        var NotifSCSP: SharedPreferences =
            context.getSharedPreferences("NotifSCSP", Context.MODE_PRIVATE)


        return NotifSCSP.getInt("NotifSCSP",0)
    }

    fun setNotifModel(context: Context, switch : Int) {

        var NotifModelSP: SharedPreferences =
            context.getSharedPreferences("NotifModelSP", Context.MODE_PRIVATE)

        NotifModelSP.edit().putInt("NotifModelSP", switch).commit()

    }

    fun getNotifModel(context: Context): Int {

        var NotifSCSP: SharedPreferences =
            context.getSharedPreferences("NotifModelSP", Context.MODE_PRIVATE)


        return NotifSCSP.getInt("NotifModelSP", 0)
    }


}