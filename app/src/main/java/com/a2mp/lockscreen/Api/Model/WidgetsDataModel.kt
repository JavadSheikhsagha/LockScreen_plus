package com.a2mp.lockscreen.Widgets

import android.widget.ImageView

data class WidgetsDataModel(var success: Boolean, var data: ResponseDataModel)

data class ResponseDataModel(
    var temperature: Int,
    var airCondition: String,
    var windDirection: Int,
    var windSpeed: Int,
    var moonPhase: MoonData,
    var precipitation: Int,
    var sunset: String,
    var sunrise: String,
    var temperature_2m_min: Int,
    var temperature_2m_max: Int,
    var europeanAqi: Int,
    var uvIndex: Int ,
    var moonrise : String ,
    var moonset : String
)

data class MoonData(var name: String, var symbol: String, var description: String)


data class LocationModel(var lat : String , var lon : String , var date : String , var timeZone : String)