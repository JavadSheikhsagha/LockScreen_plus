package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.Emoji.EmojiModel
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide

class EmojiWalpaperAdapter (
    data: MutableList<EmojiModel>,
    var onItemClick: (String ,MutableList<String> , String) -> Unit
) : RecyclerView.Adapter<EmojiWalpaperAdapter.ViewHolder>() {


    internal var wallpapersItemList: MutableList<EmojiModel>

    init {
        this.wallpapersItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_emoji_wallpaper, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.card.setOnClickListener {
            onItemClick.invoke(wallpapersItemList[position].name , wallpapersItemList[position].content , wallpapersItemList[position].background)
        }



        holder.card.setCardBackgroundColor(Color.parseColor(wallpapersItemList[position].background))


        // TODO:  set image emoji
        holder.linearLayout.setImageResource(wallpapersItemList[position].view)

    }


    override fun getItemCount(): Int {

        return wallpapersItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var linearLayout: ImageView
        lateinit var card : CardView


        init {

            linearLayout = itemView.findViewById(R.id.emoji_category_ll)
            card = itemView.findViewById(R.id.emoji_wallpaper_cv)

        }


    }


}