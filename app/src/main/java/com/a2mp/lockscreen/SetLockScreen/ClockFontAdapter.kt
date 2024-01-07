package com.a2mp.lockscreen.SetLockScreen



import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.R


class ClockFontAdapter (
    data: MutableList<Int>,
    var onItemClick : ()-> Unit
) : RecyclerView.Adapter<ClockFontAdapter.ViewHolder>() {

    private var selectedItem = 0

    internal var ClockFontItemList: MutableList<Int>

    init {
        this.ClockFontItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_clock_font_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {


        try {
            if (position == selectedItem){
                holder.backgroundTextoo.setBackgroundResource(R.drawable.background_clock_font_selected)
            }else{
                holder.backgroundTextoo.setBackgroundResource(R.drawable.background_clock_font_unselected)
            }
        }catch (e : Exception){

        }


        val typeface = ResourcesCompat.getFont(holder.itemView.context, ClockFontItemList[position])
        holder.textoo.setTypeface(typeface)

        holder.textoo.setOnClickListener {

            selectedItem = position
            onItemClick.invoke()
            notifyDataSetChanged()

        }

    }


    override fun getItemCount(): Int {

        return ClockFontItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var textoo : TextView
        lateinit var backgroundTextoo : LinearLayout

        init {

            textoo = itemView.findViewById(R.id.font_tv)
            backgroundTextoo = itemView.findViewById(R.id.font_ll)

        }


    }

    fun choosenFont() : Int{
            return selectedItem
    }


}