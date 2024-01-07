package com.a2mp.lockscreen.Widgets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class TopWidgetItemAdapter(
    data: MutableList<TopWidgetItems>,
    var onItemClick: (TopWidgetItems) -> Unit
) : RecyclerView.Adapter<TopWidgetItemAdapter.ViewHolder>() {


    internal var topWidgetItemList: MutableList<TopWidgetItems>

    init {
        this.topWidgetItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_top_widget_items, parent, false)

        return ViewHolder(layout)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (topWidgetItemList[position].img == 0){
            holder.image.visibility = View.GONE
        }else{
            holder.image.setImageResource(topWidgetItemList[position].img)
        }


        if (topWidgetItemList[position].icon == 0){
            holder.icon.visibility = View.GONE
        }else{
            holder.icon.setImageResource(topWidgetItemList[position].icon)
        }

        holder.detail.text = topWidgetItemList[position].detail

        holder.subtitle.text = topWidgetItemList[position].subtitle

        holder.ll.setOnClickListener {
            onItemClick.invoke(topWidgetItemList[position])
        }

        if (position == topWidgetItemList.size -1){
            holder.undrline.visibility = View.GONE
        }

        if (topWidgetItemList[position].subtitle == "Conditions"){
            holder.degree.visibility = View.VISIBLE
        }

    }


    override fun getItemCount(): Int {

        return topWidgetItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var image : ImageView
        lateinit var icon : ImageView
        lateinit var detail : TextView
        lateinit var subtitle : TextView
        lateinit var ll : LinearLayout
        lateinit var undrline : View
        lateinit var degree : ImageView

        init {

            image = itemView.findViewById(R.id.top_widget_items_iv)
            icon = itemView.findViewById(R.id.top_widgets_items_icon)
            detail = itemView.findViewById(R.id.top_widget_detail)
            subtitle = itemView.findViewById(R.id.top_widget_sub_title)
            ll = itemView.findViewById(R.id.top_widget_item_ll)
            undrline = itemView.findViewById(R.id.underline_top_widget)
            degree = itemView.findViewById(R.id.top_widget_degree)

        }


    }


}