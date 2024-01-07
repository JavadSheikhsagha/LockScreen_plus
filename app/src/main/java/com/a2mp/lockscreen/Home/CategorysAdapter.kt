package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.R

class CategorysAdapter (
    data: MutableList<ResponseModelLockScreen>,
    var onItemClick : (Int , String)-> Unit
) : RecyclerView.Adapter<CategorysAdapter.ViewHolder>() {


    internal var CategorysItemList: MutableList<ResponseModelLockScreen>

    init {
        this.CategorysItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_main_categorys, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


            holder.title.text = CategorysItemList[position].category.name
            // recycler har category bayad inja set besheh
            var recycler: RecyclerView = holder.recyclerView
            Log.i("TAGasdqf1w53aw", "onBindViewHolder: ${CategorysItemList[position].pictures.toMutableList()}")
            var adapter = WallpapersAdapter(CategorysItemList[position].pictures.toMutableList()){
                onItemClick.invoke(it , CategorysItemList[position].category.name)
            }
            recycler.layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = adapter



    }


    override fun getItemCount(): Int {

        var count = 0

        for (item in CategorysItemList){
            count += 1
        }

        return count

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var title : TextView
        lateinit var recyclerView: RecyclerView

        init {

            title = itemView.findViewById(R.id.title_tv)
            recyclerView = itemView.findViewById(R.id.wallpaper_rv)

        }


    }



}