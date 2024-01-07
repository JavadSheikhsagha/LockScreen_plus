package com.a2mp.lockscreen.SetLockScreen

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

class ClockColorAdapter(
    data: MutableList<String>,
    var onItemClick: () -> Unit
) : RecyclerView.Adapter<ClockColorAdapter.ViewHolder>() {

    private var selectedItem = 0

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
                holder.coloroo.setBorderColor(Color.parseColor("#E3E3E3"))
            }
            1 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#3E3E3E"))
            }
            2 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#BCBCBC"))
            }
            3 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#75BACD"))
            }
            4 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#91A9CF"))
            }
            5 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#B3A5CF"))
            }
            6 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#BD88CF"))
            }
            7 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#C68AA1"))
            }
            8 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#C68AA1"))
            }
            9 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFA19E"))
            }
            10 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFA19E"))
            }
            11 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFAA99"))
            }
            12 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFB791"))
            }
            13 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFB791"))
            }
            14 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFCDAD"))
            }
            15 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#CFCDAD"))
            }
            16 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#C5CAA2"))
            }
            17 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#C5CAA2"))
            }
            18 -> {
                holder.coloroo.setBorderColor(Color.parseColor("#AABF99"))
            }
        }

        holder.coloroo.setOnClickListener {
            selectedItem = position
            notifyDataSetChanged()
            onItemClick.invoke()
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