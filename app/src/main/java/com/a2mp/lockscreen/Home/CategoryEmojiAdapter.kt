package com.a2mp.lockscreen.Home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Api.Model.ResponseModelLockScreen
import com.a2mp.lockscreen.Emoji.EmojiModel
import com.a2mp.lockscreen.R

class CategoryEmojiAdapter  (
    data: MutableList<String>,
    var onItemClick : (String , MutableList<String> , String)-> Unit
) : RecyclerView.Adapter<CategoryEmojiAdapter.ViewHolder>() {


    internal var CategorysItemList: MutableList<String>
    internal var listEmojies : MutableList<EmojiModel>

    init {
        this.CategorysItemList = data
        this.listEmojies = mutableListOf<EmojiModel>()
        listEmojies.add(EmojiModel("Small", mutableListOf("✈","☁"),R.drawable.img_back_emoji_cat_1 , "#CAE6F4" ))
        listEmojies.add(EmojiModel("Medium", mutableListOf("\uD83D\uDD25","\uD83D\uDE0D","❣"),R.drawable.img_back_emoji_cat_2 , "#F8D3C0"))
        listEmojies.add(EmojiModel("Large", mutableListOf("\uD83E\uDDC0", "\uD83D\uDC2D"),R.drawable.img_back_emoji_cat_3 , "#DE789D"))
        listEmojies.add(EmojiModel("Spiral", mutableListOf("\uD83D\uDEF8","\uD83E\uDE90","\uD83D\uDC7D","\uD83D\uDEF8","\uD83E\uDE90"),R.drawable.img_back_emoji_cat_4 , "#353838"))
        listEmojies.add(EmojiModel("Ring", mutableListOf("\uD83D\uDE01","\uD83E\uDD7A","\uD83D\uDE00","☺", "\uD83D\uDE02" , "\uD83D\uDE0D"),R.drawable.img_back_emoji_cat_5 , "#FFEA82"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_main_categorys, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.title.text = "Emoji"
        // recycler har category bayad inja set besheh

        var recycler: RecyclerView = holder.recyclerView
        var adapter = EmojiWalpaperAdapter(listEmojies){model , emojies ,background->
            onItemClick.invoke(model , emojies, background)
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