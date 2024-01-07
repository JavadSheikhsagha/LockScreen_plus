package com.a2mp.lockscreen.Emoji

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.Home.WallpapersAdapter
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide

class EmojisItemsAdapter (
    data: MutableList<String>,
    var onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EmojisItemsAdapter.ViewHolder>() {


    internal var emojisItemsList: MutableList<String>

    init {
        this.emojisItemsList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_emoji_single, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.emoji.text = emojisItemsList[position]

        holder.emoji.setOnClickListener {
            onItemClick.invoke(emojisItemsList[position])
        }

        holder.emoji.setOnClickListener {
            onItemClick.invoke(emojisItemsList[position])
        }

    }


    override fun getItemCount(): Int {

        return emojisItemsList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var emoji: TextView

        init {

            emoji = itemView.findViewById(R.id.single_emoji_tv)


        }


    }


}