package com.a2mp.lockscreen.Widgets

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.PictureModel
import com.a2mp.lockscreen.Home.WallpapersAdapter
import com.a2mp.lockscreen.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class TopWidgetCategoryAdapter(
    data: MutableList<TopWidgetCategoryItem>,
    var onItemClick: (TopWidgetItems) -> Unit
) : RecyclerView.Adapter<TopWidgetCategoryAdapter.ViewHolder>() {


    internal var topWidgetCategoryList: MutableList<TopWidgetCategoryItem>

    init {
        this.topWidgetCategoryList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_top_widget_rv, parent, false)

        return ViewHolder(layout)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (topWidgetCategoryList[position].icon != 0){
            holder.icon.setImageResource(topWidgetCategoryList[position].icon)
        }else{
            holder.icon.visibility = View.GONE
        }

        holder.title.text = topWidgetCategoryList[position].title

        var recycler: RecyclerView = holder.recycler

        var adapter = TopWidgetItemAdapter(topWidgetCategoryList[position].items){
            onItemClick.invoke(it)
        }
        recycler.layoutManager = LinearLayoutManager(holder.recycler.context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter


    }


    override fun getItemCount(): Int {

        return topWidgetCategoryList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var icon : ImageView
        lateinit var title : TextView
        lateinit var recycler : RecyclerView

        init {

            icon = itemView.findViewById(R.id.img_category_top_widgets)
            title = itemView.findViewById(R.id.top_widget_title_tv)
            recycler = itemView.findViewById(R.id.top_widget_items_rv)


        }


    }


}