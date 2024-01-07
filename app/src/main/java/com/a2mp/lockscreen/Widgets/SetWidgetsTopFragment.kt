package com.a2mp.lockscreen.Widgets

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Home.CategorysAdapter
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.SetLockScreen.SetLockScreenMultipleActivity
import com.a2mp.lockscreen.databinding.FragmentSetWidgetsTopBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import java.text.SimpleDateFormat
import java.util.*


class SetWidgetsTopFragment(var onItemClick : (TopWidgetItems)-> Unit , var onDismisso : ()->Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentSetWidgetsTopBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme
    lateinit var categoryList : MutableList<TopWidgetCategoryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetWidgetsTopBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        var eventName = "top_widget"
        var eventParams = Bundle().apply {
            putString("param_key", "")
            // Add more parameters if needed
        }
        firebaseAnalytics.logEvent(eventName, eventParams)
        var widgetsUtility = WidgetsUtility()

        categoryList = mutableListOf()
        categoryList.add(TopWidgetCategoryItem("Suggestions" , 0 , mutableListOf(
            TopWidgetItems(R.drawable.img_weather , R.drawable.icon_sunset , "7:32AM" , "Sunrise and Sunset"),
            TopWidgetItems(R.drawable.img_weather , R.drawable.icon_moon , "First Quarter" , "Moon"),
            TopWidgetItems(R.drawable.img_calendar , R.drawable.icon_calendar , "No upcoming events" , "Next Event")
        )))
        categoryList.add(TopWidgetCategoryItem("Calendar" , R.drawable.img_calendar , mutableListOf(
            TopWidgetItems(0 , 0 , "${getDate(System.currentTimeMillis())}" , "Date"),
            TopWidgetItems(0, R.drawable.icon_calendar , "No upcoming events" , "Next Event")
        )))
        categoryList.add(TopWidgetCategoryItem("Clock" , R.drawable.img_clock , mutableListOf(
            TopWidgetItems(0 , R.drawable.icon_earth , "CUP ${getTime(System.currentTimeMillis())}" , "City")
        )))
        categoryList.add(TopWidgetCategoryItem("Weather" , R.drawable.img_weather , mutableListOf(
            TopWidgetItems(0 , R.drawable.icon_suny_cloudy , "Cupertino" , "Location"),
            TopWidgetItems(0 , R.drawable.icon_suny_cloudy , "22" , "Conditions"),
            TopWidgetItems(0 , R.drawable.icon_sun , "UVI 6 High" , "UV Index"),
            TopWidgetItems(0 , R.drawable.icon_sunset , "7:32" , "Sunrise and Sunset"),
            TopWidgetItems(0 , R.drawable.icon_moon , "First Quarter" , "Moon"),
            TopWidgetItems(0 , R.drawable.icon_umberlla , "60% Today" , "Precipitation"),
            TopWidgetItems(0 , R.drawable.icon_wind , "22mph NE" , "Wind"),
            TopWidgetItems(0 , 0 , "AQI 42" , "Air Quality")
        )))


        var recycler: RecyclerView = binding.topWidgetCateRv
        var adapter = TopWidgetCategoryAdapter(categoryList) {
            onItemClick.invoke(it)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter


        binding.escTopWidgetBtn.setOnClickListener {
            onDismisso.invoke()
            dismiss()
        }

    }

    fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat(
            "EEEE, MMMM dd"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    fun getTime(milliSeconds: Long): String {
        val formatter = SimpleDateFormat(
            "HH:mm a"
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}