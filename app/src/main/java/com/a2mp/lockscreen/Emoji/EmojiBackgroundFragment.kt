package com.a2mp.lockscreen.Emoji

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentEmojiBackgroundBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EmojiBackgroundFragment(var onFinished : (String)-> Unit) : BottomSheetDialogFragment() {

    lateinit var binding : FragmentEmojiBackgroundBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme
    lateinit var emojiBackColorList : MutableList<String>
    lateinit var adapterColor: EmojiBackgroundColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmojiBackgroundBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emojiBackColorList = mutableListOf()
        addColor()



        var recyclerColor: RecyclerView = binding.emojiBackgroundColorRv
        adapterColor = EmojiBackgroundColorAdapter(emojiBackColorList) {
            if (it != -1){
                onFinished.invoke(adapterColor.choosenColor())
                binding.gradientSeekBar.setColors(Color.parseColor("#FFFFFF"), Color.parseColor(emojiBackColorList[it]))
            }
        }
        recyclerColor.layoutManager = GridLayoutManager(requireContext(), 6)
        recyclerColor.adapter = adapterColor



        binding.gradientSeekBar.setColors(Color.parseColor("#FFFFFF"), Color.parseColor("#78D3F7"))


        binding.gradientSeekBar.listener = { offset , argb ->
            Log.i("iegqohoewq8i90", "onViewCreated: ${intToHexColor(argb)}")
            onFinished.invoke(intToHexColor(argb))
        }

        binding.dialogBackgroundColorCloseBtn.setOnClickListener {
            dismiss()
        }

    }

    fun intToHexColor(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.navigationBarColor = Color.parseColor("#FFFFFF")
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

    fun addColor(){

        emojiBackColorList.add("#78D3F7")
        emojiBackColorList.add("#7FA5F8")
        emojiBackColorList.add("#AB8DF6")
        emojiBackColorList.add("#C45FF5")
        emojiBackColorList.add("#DE789D")
        emojiBackColorList.add("#EF9486")
        emojiBackColorList.add("#F2A984")
        emojiBackColorList.add("#F5C883")
        emojiBackColorList.add("#F8DA85")
        emojiBackColorList.add("#FEF9A0")
        emojiBackColorList.add("#EAF19B")
        emojiBackColorList.add("#BADB93")
        emojiBackColorList.add("#B4C1AC")
        emojiBackColorList.add("#A3B6BC")
        emojiBackColorList.add("#B5A8B6")
        emojiBackColorList.add("#B8B1A8")
        emojiBackColorList.add("#FFFFFF")
        emojiBackColorList.add("#000000")

    }

}