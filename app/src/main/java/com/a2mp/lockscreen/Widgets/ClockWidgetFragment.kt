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
import com.a2mp.lockscreen.databinding.FragmentClockWidgetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.relex.circleindicator.CircleIndicator3


class ClockWidgetFragment(var onItemClick : (WidgetModel)-> Unit , var onDismiss : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentClockWidgetBinding
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
        binding = FragmentClockWidgetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widgetSliderItems = mutableListOf()
        widgetSliderItems.add(WidgetSliderModel("City Digital" , "Add a clock for a city to check the time at\nthat location." , mutableListOf(WidgetModel("numberClockSquare" , R.layout.style_widget_number_clock_square , 1) , WidgetModel("numberClockRectangle" , R.layout.style_widget_number_clock_rectangle , 2))))
        widgetSliderItems.add(WidgetSliderModel("City Analog" , "Add a clock for a city to check the time at\nthat location." , mutableListOf(WidgetModel("analogClockSquare" , R.layout.style_widget_analog_clock_square , 1))))
        widgetSliderItems.add(WidgetSliderModel("World Clock" , "View the time in multiple cities around the world." , mutableListOf(WidgetModel("worldClockRectangle" , R.layout.style_widget_world_clock_rectangle , 2))))

        setupViewPager()

        binding.escBtnCatClock.setOnClickListener {
            onDismiss.invoke()
            dismiss()
        }

    }

    fun setupViewPager() {



        viewPager2 = binding.clockSliderRv

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

        var indicator : CircleIndicator3 = binding.circleIndicatorClock

        indicator.setViewPager(viewPager2)



    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

}