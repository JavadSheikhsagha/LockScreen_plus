package com.a2mp.lockscreen.SetLockScreen

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentEditBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EditBottomFragment(var onFinished : (String , Int)-> Unit , var onFontSelect : (Int)-> Unit , var onColorSelect : (String)-> Unit , var onDismissoo : ()-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentEditBottomBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme
    lateinit var fontList: MutableList<Int>
    lateinit var colorList: MutableList<String>
    lateinit var adapterColor: ClockColorAdapter
    lateinit var adapterFont: ClockFontAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBottomBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorList = mutableListOf()
        addColor()

        fontList = mutableListOf()
        addFont()

        var recyclerFont: RecyclerView = binding.recyclerViewFont
        adapterFont = ClockFontAdapter(fontList) {
            onFinished.invoke(adapterColor.choosenColor() , fontList[adapterFont.choosenFont()])
        }
        recyclerFont.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerFont.adapter = adapterFont

        var recyclerColor: RecyclerView = binding.recyclerViewColor
        adapterColor = ClockColorAdapter(colorList) {
            onFinished.invoke(adapterColor.choosenColor() , fontList[adapterFont.choosenFont()])
        }
        recyclerColor.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColor.adapter = adapterColor

        binding.crossCv.setOnClickListener {
            dismiss()
        }

    }

    private fun addFont() {
        fontList.add(R.font.sf_pro_display_semibold)
        fontList.add(R.font.sf_pro_display_light)
        fontList.add(R.font.sf_compact_rounded_medium)
        fontList.add(R.font.domine_medium)
        fontList.add(R.font.finseriffisplay_bold)
        fontList.add(R.font.brocades_sans_regular)
        fontList.add(R.font.fontsfree_net_newyork)
        fontList.add(R.font.roboto_medium)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onFinished.invoke(adapterColor.choosenColor(), fontList[adapterFont.choosenFont()])
        onDismissoo.invoke()
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.navigationBarColor = Color.parseColor("#FFFFFF")
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

    fun addColor(){

        colorList.add("#FFFFFF")
        colorList.add("#454545")
        colorList.add("#E7E7E7")
        colorList.add("#90E4FC")
        colorList.add("#B3CFFF")
        colorList.add("#DCCBFF")
        colorList.add("#E8A7FF")
        colorList.add("#F4AAC5")
        colorList.add("#F4AAC5")
        colorList.add("#FEC7C2")
        colorList.add("#FEC7C2")
        colorList.add("#FFD0BC")
        colorList.add("#FEE1B3")
        colorList.add("#FEE1B3")
        colorList.add("#FFEBB8")
        colorList.add("#FEFCD5")
        colorList.add("#F2F8C8")
        colorList.add("#F2F8C8")
        colorList.add("#D1EBBC")

    }

}