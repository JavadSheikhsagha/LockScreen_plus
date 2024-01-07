package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class WallpapersAdapter(
    data: MutableList<PictureModel>,
    var onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<WallpapersAdapter.ViewHolder>() {


    internal var WallpapersItemList: MutableList<PictureModel>

    init {
        this.WallpapersItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_wallpaper_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        Picasso.get()
//            .load("http://157.90.30.203/lockscreen/public/${WallpapersItemList[position].picName}")
//            .into(holder.imageView)

        Glide.with(holder.imageView)
            .load("http://157.90.30.203/lockscreen/public/${WallpapersItemList[position].picName}")
            .thumbnail(0.28f)
            .into(holder.imageView)

        holder.cardView.setOnClickListener {
            onItemClick.invoke(position)
        }

    }


    override fun getItemCount(): Int {

        return WallpapersItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var imageView: ImageView
        lateinit var cardView: CardView

        init {

            imageView = itemView.findViewById(R.id.wallpaper_iv)
            cardView = itemView.findViewById(R.id.wallpaper_cv)


        }


    }


}