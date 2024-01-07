package com.a2mp.lockscreen.Emoji

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.R
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class SliderEmoji(
var context: Context,
data: MutableList<Int>,
viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderEmoji.ViewHolder>() {

    internal var sliderItemList: MutableList<Int>

    init {
        this.sliderItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_emoji_slider, parent, false)

        return ViewHolder(layout)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //here add view like lockcreen widget in adapter
        val view = LayoutInflater.from(holder.viewoo.context)
            .inflate(
                sliderItemList[position],
                holder.viewoo,
                false
            )

        holder.viewoo.addView(view)

    }

    fun hasChange(){
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {

        return sliderItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var viewoo: LinearLayout


        init {

            viewoo = itemView.findViewById(R.id.slider_viewwwwwwwo)

        }


    }



}