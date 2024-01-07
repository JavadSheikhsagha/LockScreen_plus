package com.a2mp.lockscreen.Emoji

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.R
import com.makeramen.roundedimageview.RoundedImageView

class EmojiBackgroundColorAdapter(
    data: MutableList<String>,
    var onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<EmojiBackgroundColorAdapter.ViewHolder>() {

    private var selectedItem = -1

    internal var ClockColorItemList: MutableList<String>

    init {
        this.ClockColorItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_clock_color_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor", "ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        try {
            if (position == selectedItem) {
                holder.backgroundoo.setBorderColor(Color.parseColor("#0B7CF2"))
            } else {
                holder.backgroundoo.setBorderColor(Color.TRANSPARENT)
            }
        } catch (e: Exception) {

        }

        holder.coloroo.background = Color.parseColor(ClockColorItemList[position]).toDrawable()

        when (position) {
            0 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#6CBEDE"))
            }
            1 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#7295DF"))
            }
            2 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#9A7FDE"))
            }
            3 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#B055DD"))
            }
            4 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#C76C8D"))
            }
            5 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#D88578"))
            }
            6 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#DA9877"))
            }
            7 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#DDB476"))
            }
            8 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#DFC478"))
            }
            9 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#E4E090"))
            }
            10 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#D3D98B"))
            }
            11 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#A7C585"))
            }
            12 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#A2AE9B"))
            }
            13 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#92A4A9"))
            }
            14 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#A397A4"))
            }
            15 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#A5A097"))
            }
            16 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#E5E5E5"))
            }
            17 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#000000"))
            }
        }

        holder.coloroo.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            onItemClick.invoke(position)
        }

    }


    override fun getItemCount(): Int {

        return ClockColorItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var coloroo: RoundedImageView
        lateinit var backgroundoo: RoundedImageView

        init {

            coloroo = itemView.findViewById(R.id.color_spot)
            backgroundoo = itemView.findViewById(R.id.selected_background)

        }


    }

    fun choosenColor(): String {
        return ClockColorItemList[selectedItem]
    }


}