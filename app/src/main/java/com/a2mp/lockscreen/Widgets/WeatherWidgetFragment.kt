package com.a2mp.lockscreen.Widgets

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.MyScreens.DeleteFragment
import com.a2mp.lockscreen.MyScreens.SliderAdapter
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentWeatherWidgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.relex.circleindicator.CircleIndicator3


class WeatherWidgetFragment(var onItemClick : (WidgetModel)-> Unit , var onDismiss : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentWeatherWidgetBinding
    lateinit var viewPager2: ViewPager2
    var sliderHandler: Handler = Handler()
    lateinit var adapteer : WidgetsSliderAdapter
    lateinit var widgetSliderItems : MutableList<WidgetSliderModel>
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherWidgetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widgetSliderItems = mutableListOf()
        widgetSliderItems.add(WidgetSliderModel("Temperature" , "See the current temperature for your location." , mutableListOf(WidgetModel("weatherTemperatureSquare" ,R.layout.style_widget_weather_temperature_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Conditions" , "See the current weather conditions for\nyour location." , mutableListOf(WidgetModel("weatherConditionRectangle" ,R.layout.style_widget_weather_conditions_rectangle , 2))))
        widgetSliderItems.add(WidgetSliderModel("UV Index" , "See the current UV index for your location." , mutableListOf(WidgetModel("weatherUvindexSquare" ,R.layout.style_widget_weather_uvindex_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Sunrise and Sunset" , "See the upcoming sunrise or sunset for\nyour location." , mutableListOf(WidgetModel("weatherSunStatusSquare" ,R.layout.style_widget_weather_sun_status_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Precipitation" , "See rain, snow, sleet, or hail forecasted for\nyour location." , mutableListOf(WidgetModel("weatherPrecipitationSquare" ,R.layout.style_widget_weather_precipitation_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Wind" , "See the wind speed and direction for\nyour location." , mutableListOf(WidgetModel("weatherWindSquare" ,R.layout.style_widget_weather_wind_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Air Quality" , "See the current air quality index for your location.\nAvailable in some countries and regions." , mutableListOf(WidgetModel("weatherAirQualitySquare" ,R.layout.style_widget_weather_airquality_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("Moon" , "See the current phase and the upcoming\nrise or set for your location." , mutableListOf(WidgetModel("weatherMoonRectangle" ,R.layout.style_widget_weather_moon_rectangle , 2))))

        setupViewPager()


        binding.escBtnCatWeather.setOnClickListener {
            onDismiss.invoke()
            dismiss()
        }

    }

    fun setupViewPager() {



        viewPager2 = binding.weatherSliderRv
        adapteer = WidgetsSliderAdapter(widgetSliderItems, viewPager2){
            onItemClick.invoke(it)
        }



        viewPager2.setAdapter(adapteer)

        viewPager2.setClipToPadding(false)
        viewPager2.setClipChildren(false)
        viewPager2.setOffscreenPageLimit(3)
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)


        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(90))

        viewPager2.setPageTransformer(compositePageTransformer)

        var indicator : CircleIndicator3 = binding.circleIndicatorWeather

        indicator.setViewPager(viewPager2)



    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}