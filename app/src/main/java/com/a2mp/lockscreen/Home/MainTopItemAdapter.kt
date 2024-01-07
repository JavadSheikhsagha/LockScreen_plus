package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.R

class MainTopItemAdapter (
    data: MutableList<TopItemModel>,
    var onItemlick : (Int)-> Unit
) : RecyclerView.Adapter<MainTopItemAdapter.ViewHolder>() {

    internal var MainTopItemList: MutableList<TopItemModel>

    init {
        this.MainTopItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_main_top_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.image.setImageResource(MainTopItemList[position].icon)


        holder.text.text = MainTopItemList[position].title

        holder.item.setOnClickListener {
            onItemlick.invoke(position)
        }

    }


    override fun getItemCount(): Int {

        return MainTopItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var image : ImageView
        lateinit var text : TextView
        lateinit var item : LinearLayout


        init {

            image = itemView.findViewById(R.id.main_top_iv)
            text = itemView.findViewById(R.id.main_top_tv)
            item = itemView.findViewById(R.id.top_main_ll)

        }


    }



}