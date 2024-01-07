package com.a2mp.lockscreen.Widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentSetWidgetsBinding
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics


class SetWidgetsBottomFragment(var onItemClick: (WidgetModel) -> Unit , var OnDisMisso : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentSetWidgetsBinding
    lateinit var batterieWidgetFragment: BatterieWidgetFragment
    lateinit var weatherWidgetFragment: WeatherWidgetFragment
    lateinit var clockWidgetFragment: ClockWidgetFragment
    lateinit var calendarWidgetFragment: CalendarWidgetFragment
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetWidgetsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        var eventName = "bottom_widget"
        var eventParams = Bundle().apply {
            putString("param_key", "")
            // Add more parameters if needed
        }
        firebaseAnalytics.logEvent(eventName, eventParams)
        batterieWidgetFragment = BatterieWidgetFragment({
            onItemClick.invoke(it)
        } , {
            binding.addWidgetsAll.visibility = View.VISIBLE
        })

        weatherWidgetFragment = WeatherWidgetFragment ({
            onItemClick.invoke(it)
        } , {
            binding.addWidgetsAll.visibility = View.VISIBLE
        })

        clockWidgetFragment = ClockWidgetFragment({
            onItemClick.invoke(it)
        } , {
            binding.addWidgetsAll.visibility = View.VISIBLE
        })

        calendarWidgetFragment = CalendarWidgetFragment({
            onItemClick.invoke(it)
        } , {
            binding.addWidgetsAll.visibility = View.VISIBLE
        })


        binding.batteryCateLl.setOnClickListener {

            if (!batterieWidgetFragment.isAdded) {
                batterieWidgetFragment.show(requireActivity().supportFragmentManager, "")
                binding.addWidgetsAll.visibility = View.INVISIBLE
            }

        }

        binding.weatherCateLl.setOnClickListener {
            if (!weatherWidgetFragment.isAdded) {
                weatherWidgetFragment.show(requireActivity().supportFragmentManager, "")
                binding.addWidgetsAll.visibility = View.INVISIBLE
            }
        }

        binding.clockCateLl.setOnClickListener {
            if (!clockWidgetFragment.isAdded) {
                clockWidgetFragment.show(requireActivity().supportFragmentManager, "")
                binding.addWidgetsAll.visibility = View.INVISIBLE
            }
        }

        binding.calendarCateLl.setOnClickListener {
            if (!calendarWidgetFragment.isAdded) {
                calendarWidgetFragment.show(requireActivity().supportFragmentManager, "")
                binding.addWidgetsAll.visibility = View.INVISIBLE
            }
        }

        binding.addWidgetBatteryOne.setOnClickListener {
            onItemClick.invoke(WidgetModel("batterySquare" , R.layout.style_widget_battery_square , 1))
        }
        binding.addWidgetBatteryTwo.setOnClickListener {
            onItemClick.invoke(WidgetModel("batteryRectangle" , R.layout.style_widget_battery_rectangle , 2))
        }

        binding.addWidgetCalendarOne.setOnClickListener {
            onItemClick.invoke(WidgetModel("calendarRectangle" , R.layout.style_widget_calendar_rectangle , 2))
        }
        binding.addWidgetCalendarTwo.setOnClickListener {
            onItemClick.invoke(WidgetModel("calendarSquare" , R.layout.style_widget_calendar_square , 1))
        }

        binding.addWidgetClockOne.setOnClickListener {
            onItemClick.invoke(WidgetModel("numberClockSquare" , R.layout.style_widget_number_clock_square , 1))
        }
        binding.addWidgetClockTwo.setOnClickListener {
            onItemClick.invoke(WidgetModel("analogClockSquare" , R.layout.style_widget_analog_clock_square , 1))
        }
        binding.addWidgetClockThree.setOnClickListener {
            onItemClick.invoke(WidgetModel("worldClockRectangle" , R.layout.style_widget_world_clock_rectangle , 2))
        }
        binding.addWidgetClockFour.setOnClickListener {
            onItemClick.invoke(WidgetModel("numberClockRectangle" , R.layout.style_widget_number_clock_rectangle , 2))
        }

        binding.addWidgetWeatherOne.setOnClickListener {
            onItemClick.invoke(WidgetModel("weatherTemperatureSquare" ,R.layout.style_widget_weather_temperature_square , 1))
        }
        binding.addWidgetWeatherTwo.setOnClickListener {
            onItemClick.invoke(WidgetModel("weatherUvindexSquare" ,R.layout.style_widget_weather_uvindex_square , 1))
        }
        binding.addWidgetWeatherThree.setOnClickListener {
            onItemClick.invoke(WidgetModel("weatherWindSquare" ,R.layout.style_widget_weather_wind_square , 1))
        }
        binding.addWidgetWeatherFour.setOnClickListener {
            onItemClick.invoke(WidgetModel("weatherSunStatusSquare" ,R.layout.style_widget_weather_sun_status_square , 1))
        }

        batterieWidgetFragment.isCancelable = false
        weatherWidgetFragment.isCancelable = false
        clockWidgetFragment.isCancelable = false
        calendarWidgetFragment.isCancelable = false

        binding.escBtnAddWidgets.setOnClickListener {

            OnDisMisso.invoke()
            dismiss()

        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}