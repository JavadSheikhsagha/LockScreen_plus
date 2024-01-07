package com.a2mp.lockscreen.Widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.owl93.dpb.CircularProgressView
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar
import com.ssarcseekbar.app.GaugeSeekBar
import java.text.SimpleDateFormat
import java.util.*


class WidgetsUtility {

    fun getHrMoon(context: Context, milliSeconds: Long): Int {
        val formatter = SimpleDateFormat(
            "HH"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime()).toInt() - MainLockScreenData.getWidgetDataSP(
            context
        ).data.moonrise.substring(0, 2).toInt()
    }

    fun getMinMoon(context: Context, milliSeconds: Long): Int {
        val formatter = SimpleDateFormat(
            "mm"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime()).toInt() - MainLockScreenData.getWidgetDataSP(
            context
        ).data.moonrise.substring(3, 5).toInt()

    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(c.uppercaseChar())
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    fun getTime(milliSeconds: Long): String {
        val formatter = SimpleDateFormat(
            "HH:mm a"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat(
            "EEEE, MMMM dd"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    fun getBatteryPercentage(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= 21) {
            val bm = context.getSystemService(AppCompatActivity.BATTERY_SERVICE) as BatteryManager
            bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus: Intent = context.registerReceiver(null, iFilter)!!
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = (level?.div(scale?.toDouble()!!))
            return (batteryPct!! * 100).toInt()
        }
    }

    fun setBottomWidgetDetails(view : View, context: Context, widget: String) {


            if (widget != "null") {
                when (widget) {
                    "analogClockSquare" -> {
                        view.findViewById<TextView>(R.id.widget_clock_analog_country_name_tv).text =
                            "${MainLockScreenData.getLocationSP(context).timeZone.substring(0, 3)}"
                    }
                    "batteryRectangle" -> {
                        view.findViewById<TextView>(R.id.widget_battery_precent_rec_tv).text =
                            "%${getBatteryPercentage(context)}"
                        view.findViewById<TextView>(R.id.widget_battery_phone_name_rec_tv).text =
                            "Your Phone"
                        view.findViewById<RoundedHorizontalProgressBar>(R.id.widget_battery_req_progress_bar).progress =
                            getBatteryPercentage(context)
                    }
                    "batterySquare" -> {
                        view.findViewById<CircularProgressView>(R.id.widget_battery_squ_progress_bar).progress =
                            getBatteryPercentage(context).toFloat()
                    }
                    "calendarRectangle" -> {
                        view.findViewById<TextView>(R.id.widget_calendar_req_date_tv).text =
                            "${getDate(System.currentTimeMillis())}"
                        view.findViewById<TextView>(R.id.widget_calendar_req_event).text =
                            "No more events today"
                    }
                    "calendarSquare" -> {

                    }
                    "numberClockRectangle" -> {
                        view.findViewById<TextView>(R.id.widget_clock_number_req_tv).text =
                            "${getTime(System.currentTimeMillis())}"
                    }
                    "numberClockSquare" -> {
                        view.findViewById<TextView>(R.id.widget_clock_squ_country_name_tv).text =
                            "${MainLockScreenData.getLocationSP(context).timeZone.substring(0, 3)}"
                        view.findViewById<TextView>(R.id.widget_clock_squ_number_tv).text =
                            "${getTime(System.currentTimeMillis()).substring(0, 5)}"
                        view.findViewById<TextView>(R.id.widget_clock_squ_ampm_tv).text =
                            "${getTime(System.currentTimeMillis()).substring(6, 8)}"
                    }
                    "weatherAirQualitySquare" -> {
                        view.findViewById<GaugeSeekBar>(R.id.widget_weather_squ_aqi_progress)
                            .setProgress(MainLockScreenData.getWidgetDataSP(context).data.europeanAqi.toFloat() / 100)
                        view.findViewById<TextView>(R.id.widget_weather_squ_aqi_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.europeanAqi}"
                    }
                    "weatherConditionRectangle" -> {
                        view.findViewById<TextView>(R.id.widget_weather_req_temp_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.temperature}"
                        view.findViewById<TextView>(R.id.widget_weather_req_clock_tv).text =
                            "${getTime(System.currentTimeMillis())}"
                        view.findViewById<TextView>(R.id.widget_weather_req_h_tv).text =
                            "H:${MainLockScreenData.getWidgetDataSP(context).data.temperature_2m_max}"
                        view.findViewById<TextView>(R.id.widget_weather_req_l_tv).text =
                            "L:${MainLockScreenData.getWidgetDataSP(context).data.temperature_2m_min}"
                    }
                    "weatherMoonRectangle" -> {
                        view.findViewById<TextView>(R.id.widget_weather_req_moon_status_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.moonPhase.name}"
                        view.findViewById<TextView>(R.id.widget_weather_req_moonrise_tv).text =
                            "Moonrise ${MainLockScreenData.getWidgetDataSP(context).data.moonrise}"
                        view.findViewById<TextView>(R.id.widget_weather_req_moonrise_ampm_tv).text =
                            "PM"
                        try {
                            view.findViewById<TextView>(R.id.widget_weather_req_until_moonrise_hour).text =
                                "${getHrMoon(context, System.currentTimeMillis())}"
                            view.findViewById<TextView>(R.id.widget_weather_req_until_moonrise_minute).text =
                                "${getMinMoon(context, System.currentTimeMillis())}"
                        } catch (e: Exception) {
                            view.findViewById<TextView>(R.id.widget_weather_req_until_moonrise_hour).text =
                                "0"
                            view.findViewById<TextView>(R.id.widget_weather_req_until_moonrise_minute).text =
                                "0"
                        }

                    }
                    "weatherPrecipitationSquare" -> {
                        view.findViewById<GaugeSeekBar>(R.id.widget_weather_squ_precipitatioin_progress)
                            .setProgress(MainLockScreenData.getWidgetDataSP(context).data.precipitation.toFloat() / 100)
                        view.findViewById<TextView>(R.id.widget_weather_squ_precipitatioin_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.precipitation}"
                    }
                    "weatherSunStatusSquare" -> {
                        view.findViewById<TextView>(R.id.widget_weather_squ_sunset_time_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.sunset}"
                        view.findViewById<TextView>(R.id.widget_weather_squ_sunset_ampm_tv).text =
                            "PM"
                    }
                    "weatherTemperatureSquare" -> {
                        view.findViewById<GaugeSeekBar>(R.id.widget_weather_squ_temp_progress)
                            .setProgress(MainLockScreenData.getWidgetDataSP(context).data.temperature.toFloat() / 10)


                        view.findViewById<TextView>(R.id.widget_weather_squ_temp_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.temperature}"
                        view.findViewById<TextView>(R.id.widget_weather_squ_temp_h_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.temperature_2m_max}"
                        view.findViewById<TextView>(R.id.widget_weather_squ_temp_l_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.temperature_2m_min}"
                    }
                    "weatherUvindexSquare" -> {
                        view.findViewById<GaugeSeekBar>(R.id.widget_weather_squ_uvindex_progress)
                            .setProgress(MainLockScreenData.getWidgetDataSP(context).data.uvIndex.toFloat() / 10)
                        view.findViewById<TextView>(R.id.widget_weather_squ_uvindex_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.uvIndex}"
                    }
                    "weatherWindSquare" -> {
                        view.findViewById<TextView>(R.id.widget_weather_squ_wind_direction_tv).text =
                            "NE"
                        view.findViewById<TextView>(R.id.widget_weather_squ_wind_speed_tv).text =
                            "${MainLockScreenData.getWidgetDataSP(context).data.windSpeed}"
                    }
                    "worldClockRectangle" -> {

                    }
                }
            }


    }

    fun setTopWidgetDetails(context: Context, topWidgetList: String): String {


        when (topWidgetList) {
            "City" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.sunset}PM"
            }
            "Moon" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.moonPhase.name}"
            }
            "Next Event" -> {
                return "No upcoming events"
            }
            "Date" -> {
                return "${getDate(System.currentTimeMillis())}"
            }
            "Next Event" -> {
                return "No upcoming events"
            }
            "City" -> {
                return "${
                    MainLockScreenData.getLocationSP(context).timeZone.substring(
                        0,
                        3
                    )
                } ${getTime(System.currentTimeMillis())}"
            }
            "Location" -> {
                return "${MainLockScreenData.getLocationSP(context).timeZone}"
            }
            "Conditions" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.airCondition}"
            }
            "UV Index" -> {
                return "UVI ${MainLockScreenData.getWidgetDataSP(context).data.uvIndex} High"
            }
            "Sunrise and Sunset" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.sunset}"
            }
            "Moon" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.moonPhase.name}"
            }
            "Precipitation" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.precipitation}% Today"
            }
            "Wind" -> {
                return "${MainLockScreenData.getWidgetDataSP(context).data.windSpeed}mph"
            }
            "Air Quality" -> {
                return "AQI ${MainLockScreenData.getWidgetDataSP(context).data.europeanAqi}"
            }
            else -> {
                return ""
            }
        }

    }

    fun returnViewFromName(widget: String): Int {


        when (widget) {
            "analogClockSquare" -> {
                return R.layout.style_widget_analog_clock_square
            }
            "batteryRectangle" -> {
                return R.layout.style_widget_battery_rectangle
            }
            "batterySquare" -> {
                return R.layout.style_widget_battery_square
            }
            "calendarRectangle" -> {
                return R.layout.style_widget_calendar_rectangle
            }
            "calendarSquare" -> {
                return R.layout.style_widget_calendar_square
            }
            "numberClockRectangle" -> {
                return R.layout.style_widget_number_clock_rectangle
            }
            "numberClockSquare" -> {
                return R.layout.style_widget_number_clock_square
            }
            "weatherAirQualitySquare" -> {
                return R.layout.style_widget_weather_airquality_square
            }
            "weatherConditionRectangle" -> {
                return R.layout.style_widget_weather_conditions_rectangle
            }
            "weatherMoonRectangle" -> {
                return R.layout.style_widget_weather_moon_rectangle
            }
            "weatherPrecipitationSquare" -> {
                return R.layout.style_widget_weather_precipitation_square
            }
            "weatherSunStatusSquare" -> {
                return R.layout.style_widget_weather_sun_status_square
            }
            "weatherTemperatureSquare" -> {
                return R.layout.style_widget_weather_temperature_square
            }
            "weatherUvindexSquare" -> {
                return R.layout.style_widget_weather_uvindex_square
            }
            "weatherWindSquare" -> {
                return R.layout.style_widget_weather_wind_square
            }
            "worldClockRectangle" -> {
                return R.layout.style_widget_world_clock_rectangle
            }
            else -> {
                return 0
            }
        }


    }



}