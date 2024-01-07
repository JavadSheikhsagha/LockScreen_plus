package com.a2mp.lockscreen.Emoji

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.databinding.FragmentEmojiBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray


class EmojiBottomSheetFragment(var selectedEmoji : MutableList<String>,var onEmojiClick : (String)->Unit , var onEmojiDelete : ()->Unit) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentEmojiBottomSheetBinding
    override fun getTheme() = R.style.NoBackgroundDialogTheme
//    lateinit var listEojies: MutableList<EmojiKeyboardModel>
    lateinit var recycler: RecyclerView
    lateinit var adapter: EmojisItemsAdapter
    lateinit var mEmojiViewModel: EmojiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmojiBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (e in selectedEmoji){
            binding.selectedEmojisDialog.text = "${binding.selectedEmojisDialog.text}$e"
        }

        binding.emojiFieldCapacityTv.text = "${selectedEmoji.size}/6"
        mEmojiViewModel = ViewModelProvider(this).get(EmojiViewModel::class.java)

        var currentCat = 0


            mEmojiViewModel.readAllData.observe(requireActivity()){
                CoroutineScope(Dispatchers.Main).launch {

                    withContext(Dispatchers.Main){
                        for (item in it){
                            val view = LayoutInflater.from(binding.emojiMainLl.context)
                                .inflate(
                                    R.layout.style_emoji_rv,
                                    binding.emojiMainLl,
                                    false
                                )

                            view.findViewById<TextView>(R.id.emoji_catefory_title).text = item.name

                            recycler = view.findViewById<RecyclerView>(R.id.emoji_items_rv)

                            adapter = EmojisItemsAdapter(item.value.split(",")?.toMutableList()) {

                                if (selectedEmoji.size < 6){
                                    onEmojiClick.invoke(it)
                                    binding.selectedEmojisDialog.text = "${binding.selectedEmojisDialog.text}$it"
                                    binding.emojiFieldCapacityTv.text = "${selectedEmoji.size}/6"
                                }

                            }
                            recycler.layoutManager = GridLayoutManager(
                                recycler.context, 5,
                                GridLayoutManager.HORIZONTAL, false
                            )
                            recycler.adapter = adapter


                            binding.emojiMainLl.addView(view)
                        }
                    }


                }
            }





        binding.deleteEmojisIv.setOnClickListener {
            if (selectedEmoji.size > 0){
                onEmojiDelete.invoke()
                binding.emojiFieldCapacityTv.text = "${selectedEmoji.size}/6"
                binding.selectedEmojisDialog.text = ""
                for (e in selectedEmoji){
                    binding.selectedEmojisDialog.text = "${binding.selectedEmojisDialog.text}$e"
                }
            }
        }

        binding.dialogEmojiCloseBtn.setOnClickListener {
            dismiss()
        }


        binding.nsv.viewTreeObserver.addOnScrollChangedListener {
            val scrollX = binding.nsv.scrollX
            val scrollY = binding.nsv.scrollY

            if (scrollX in 0..1159) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 0
                binding.frequentluUseCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_clock_selected)
            }
            if (scrollX in 1160..11613) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 1
                binding.catEmojiCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_emoji_selected)
            }
            if (scrollX in 11614..16351) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 2
                binding.catAnimalCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_animal_selected)
            }
            if (scrollX in 16352..19293) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 3
                binding.catFoodCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_food_selected)
            }
            if (scrollX in 19294..22015) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 4
                binding.catBallCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_ball_selected)
            }
            if (scrollX in 22016..25179) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 5
                binding.catCityCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_city_selected)
            }
            if (scrollX in 25180..30593) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 6
                binding.catLampCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_lamp_selected)
            }
            if (scrollX in 30594..37348) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 7
                binding.catQwertCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_qwert_selected)
            }
            if (scrollX > 37349) {
                findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
                (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
                currentCat = 8
                binding.catFlagCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
                (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_flag_selected)
            }

            Log.d("Scroll1232ewqdwqwew", "X: $scrollX, Y: $scrollY")
        }


        binding.frequentluUseLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.frequentluUseCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(0, 0)
            currentCat = 0
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_clock_selected)
        }
        binding.catEmojiLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catEmojiCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(1160, 0)
            currentCat = 1
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_emoji_selected)
        }
        binding.catAnimalLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catAnimalCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(11614, 0)
            currentCat = 2
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_animal_selected)
        }
        binding.catFoodLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catFoodCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(16352, 0)
            currentCat = 3
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_food_selected)
        }
        binding.catBallLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catBallCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(19294, 0)
            currentCat = 4
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_ball_selected)
        }
        binding.catCityLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catCityCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(22016, 0)
            currentCat = 5
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_city_selected)
        }
        binding.catLampLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catLampCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(25180, 0)
            currentCat = 6
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_lamp_selected)
        }
        binding.catQwertLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catQwertCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(30594, 0)
            currentCat = 7
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_qwert_selected)
        }
        binding.catFlagLl.setOnClickListener {
            findCat(currentCat).setBackgroundResource(R.drawable.background_emoji_category_unselected)
            (findImg(currentCat).view as ImageView).setImageResource(findImg(currentCat).drawble)
            binding.catFlagCv.setBackgroundResource(R.drawable.background_emoji_category_selected)
            binding.nsv.scrollTo(37349, 0)
            currentCat = 8
            (findImg(currentCat).view as ImageView).setImageResource(R.drawable.icon_flag_selected)
        }


    }

    fun findCat(int: Int): View {

        when (int) {
            0 -> {
                return binding.frequentluUseCv
            }
            1 -> {
                return binding.catEmojiCv
            }
            2 -> {
                return binding.catAnimalCv
            }
            3 -> {
                return binding.catFoodCv
            }
            4 -> {
                return binding.catBallCv
            }
            5 -> {
                return binding.catCityCv
            }
            6 -> {
                return binding.catLampCv
            }
            7 -> {
                return binding.catQwertCv
            }
            8 -> {
                return binding.catFlagCv
            }
            else -> {
                return binding.frequentluUseCv
            }
        }

    }
    fun findImg(int: Int): CategoryModel {

        when (int) {
            0 -> {
                return CategoryModel(binding.frequentluUseIv , R.drawable.icon_clockk)
            }
            1 -> {
                return CategoryModel(binding.catEmojiIv, R.drawable.icon_emoji)
            }
            2 -> {
                return CategoryModel(binding.catAnimalIv, R.drawable.icon_animal)
            }
            3 -> {
                return CategoryModel(binding.catFoodIv, R.drawable.icon_food)
            }
            4 -> {
                return CategoryModel(binding.catBallIv, R.drawable.icon_ball)
            }
            5 -> {
                return CategoryModel(binding.catCityIv, R.drawable.icon_city)
            }
            6 -> {
                return CategoryModel(binding.catLampIv, R.drawable.icon_lamp)
            }
            7 -> {
                return CategoryModel(binding.catQwertIv, R.drawable.icon_qwert)
            }
            8 -> {
                return CategoryModel(binding.catFlagIv, R.drawable.icon_flag)
            }
            else -> {
                return CategoryModel(binding.frequentluUseIv, R.drawable.icon_clockk)
            }
        }

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.navigationBarColor = Color.parseColor("#FFFFFF")
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

}