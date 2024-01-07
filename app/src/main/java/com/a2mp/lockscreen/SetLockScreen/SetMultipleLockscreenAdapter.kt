package com.a2mp.lockscreen.SetLockScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.MyScreens.SliderAdapter
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class SetMultipleLockscreenAdapter (
    data: MutableList<PictureModel>,
    viewPager2: ViewPager2,
    var onAddLockscreenClick : ()-> Unit
) : RecyclerView.Adapter<SetMultipleLockscreenAdapter.ViewHolder>() {

    internal var MultipleLockscreenItemList: MutableList<PictureModel>

    init {
        this.MultipleLockscreenItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_multiple_lockscreen_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.imageBackoo)
            .load("http://157.90.30.203/lockscreen/public/${MultipleLockscreenItemList[position].picName}")
            .thumbnail(0.70f)
            .into(holder.imageBackoo)

//        Picasso.get()
//            .load("http://157.90.30.203/lockscreen/public/${MultipleLockscreenItemList[position].picName}")
//            .into(holder.imageBackoo)
//
//        Picasso.get()
//            .load("http://157.90.30.203/lockscreen/public/${MultipleLockscreenItemList[position].remBgPicName}")
//            .into(holder.imageFrantoo)


    }


    override fun getItemCount(): Int {

        return MultipleLockscreenItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var imageBackoo : ImageView
        lateinit var imageFrantoo : ImageView

        init {

            imageBackoo = itemView.findViewById(R.id.image_backoo_m)
            imageFrantoo = itemView.findViewById(R.id.image_frantoo_m)

        }


    }



}