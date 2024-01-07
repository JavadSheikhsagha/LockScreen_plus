package com.a2mp.lockscreen.MyScreens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DeleteFragment(var positiono : Int ,var onDeleteClick : (Int)-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDeleteBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeleteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteDeleteLl.setOnClickListener {
            onDeleteClick.invoke(positiono)
            dismiss()
        }

        binding.deleteCancelCv.setOnClickListener {
            dismiss()
        }


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

}