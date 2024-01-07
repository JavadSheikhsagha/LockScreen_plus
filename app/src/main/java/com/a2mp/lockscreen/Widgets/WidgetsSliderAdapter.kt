package com.a2mp.lockscreen.Widgets


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.R
import com.ssarcseekbar.app.GaugeSeekBar


class WidgetsSliderAdapter(
    data: MutableList<WidgetSliderModel>,
    viewPager2: ViewPager2,
    var onItemClick: (WidgetModel) -> Unit
) : RecyclerView.Adapter<WidgetsSliderAdapter.ViewHolder>() {

    internal var widgetItemList: MutableList<WidgetSliderModel>


    init {
        this.widgetItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_slider_widgets_rv, parent, false)

        return ViewHolder(layout)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.title.text = widgetItemList[position].title
        holder.subtitle.text = widgetItemList[position].subTitle

        if (widgetItemList[position].widget.size == 1) {
            val view = LayoutInflater.from(holder.ll.context)
                .inflate(widgetItemList[position].widget[0].view, holder.ll, false)

            view.findViewById<ImageView>(R.id.click_here)?.setOnClickListener {
                onItemClick.invoke(widgetItemList[position].widget[0])
            }

            holder.ll.addView(view)
            view.findViewById<CardView>(R.id.widget_remove_cv).visibility = View.GONE
            view.setOnClickListener {
                onItemClick.invoke(widgetItemList[position].widget[0])
            }


        } else {
            val view1 = LayoutInflater.from(holder.ll.context)
                .inflate(widgetItemList[position].widget[0].view, holder.ll, false)
            holder.ll.addView(view1)
            view1.findViewById<CardView>(R.id.widget_remove_cv).visibility = View.GONE
            view1.setOnClickListener {
                onItemClick.invoke(widgetItemList[position].widget[0])
            }
            val viewEmpty = LayoutInflater.from(holder.ll.context)
                .inflate(R.layout.style_space_empty, holder.ll, false)
            holder.ll.addView(viewEmpty)
            val view2 = LayoutInflater.from(holder.ll.context)
                .inflate(widgetItemList[position].widget[1].view, holder.ll, false)
            view2.findViewById<CardView>(R.id.widget_remove_cv).visibility = View.GONE
            holder.ll.addView(view2)
            view2.setOnClickListener {
                onItemClick.invoke(widgetItemList[position].widget[1])
            }
        }


    }


    override fun getItemCount(): Int {

        return widgetItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var title: TextView
        lateinit var subtitle: TextView
        lateinit var ll: LinearLayout


        init {

            ll = itemView.findViewById(R.id.widgets)
            title = itemView.findViewById(R.id.widget_slider_title)
            subtitle = itemView.findViewById(R.id.widget_slider_subtitle)

        }


    }


}

