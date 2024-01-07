package com.a2mp.lockscreen.Widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentCalendarWidgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CalendarWidgetFragment(var onItemClick : (WidgetModel)-> Unit  , var onDismiss : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentCalendarWidgetBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarWidgetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.widgetCalendarOne.setOnClickListener {

            onItemClick.invoke(WidgetModel("calendarSquare" , R.layout.style_widget_calendar_square , 1))

        }

        binding.widgetCalendarTwo.setOnClickListener {

            onItemClick.invoke(WidgetModel("calendarRectangle" , R.layout.style_widget_calendar_rectangle , 2))

        }

        binding.escBtnCatCalendar.setOnClickListener {
            onDismiss.invoke()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}