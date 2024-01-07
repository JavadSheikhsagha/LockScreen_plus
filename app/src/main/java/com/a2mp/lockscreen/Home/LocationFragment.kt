package com.a2mp.lockscreen.Home

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentLocationBinding


class LocationFragment(var onAcceptClick : ()-> Unit , var onCancelClick : ()-> Unit ) : DialogFragment() {

    lateinit var binding : FragmentLocationBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.locationAcceptLl.setOnClickListener {

            onAcceptClick.invoke()
            dismiss()

        }

        binding.locationCancelLl.setOnClickListener {

            onCancelClick.invoke()
            dismiss()

        }


    }



}