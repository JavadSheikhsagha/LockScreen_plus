package com.a2mp.lockscreen.Widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentWidgetIsFullBinding


class WidgetIsFullFragment : DialogFragment() {

    lateinit var binding : FragmentWidgetIsFullBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWidgetIsFullBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.okLl.setOnClickListener {
            dismiss()
        }

    }

}