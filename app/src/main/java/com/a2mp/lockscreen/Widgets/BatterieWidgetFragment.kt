package com.a2mp.lockscreen.Widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentBatterieWidgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BatterieWidgetFragment(var onItemClick : (WidgetModel)-> Unit , var onDismiss : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBatterieWidgetBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBatterieWidgetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.batteryWidgetOne.setOnClickListener {

            onItemClick.invoke(WidgetModel("batterySquare" , R.layout.style_widget_battery_square , 1))

        }


        binding.batteryWidgetTwo.setOnClickListener {

            onItemClick.invoke(WidgetModel("batteryRectangle" , R.layout.style_widget_battery_rectangle , 2))

        }

        binding.escBtnCatBattery.setOnClickListener {
            onDismiss.invoke()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}