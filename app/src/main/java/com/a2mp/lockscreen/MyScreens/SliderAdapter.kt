package com.a2mp.lockscreen.MyScreens


import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.R
import com.a2mp.lockscreen.Widgets.WidgetsUtility
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class SliderAdapter(
    var context: Context,
    data: MutableList<LockScreen>,
    viewPager2: ViewPager2,
    var onDeleteClick: (Int) -> Unit,
    var onItemClick: (LockScreen) -> Unit
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    internal var SliderItemList: MutableList<LockScreen>
    var widgetsUtility = WidgetsUtility()

    init {
        this.SliderItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_slider_items, parent, false)

        return ViewHolder(layout)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (SliderItemList[position].isEmoje == true){
            holder.imageBack.setBackgroundColor(Color.parseColor(SliderItemList[position].backColor!!))
            holder.imgeEmoji.setImageBitmap(BitmapFactory.decodeByteArray(SliderItemList[position].emoji, 0, SliderItemList[position].emoji!!.size))
        }else{
            if (SliderItemList[position].imageBack.contains("/")) {

                holder.imageBack.setImageURI(Uri.parse(SliderItemList[position].imageBack))
                holder.imageFront.setImageURI(Uri.parse(SliderItemList[position].imageFront))

            } else {

                Glide.with(holder.imageBack)
                    .load("http://157.90.30.203/lockscreen/public/${SliderItemList[position].imageBack}")
                    .thumbnail(0.70f)
                    .into(holder.imageBack)

                Glide.with(holder.imageFront)
                    .load("http://157.90.30.203/lockscreen/public/${SliderItemList[position].imageFront}")
                    .thumbnail(0.70f)
                    .into(holder.imageFront)

//            Picasso.get()
//                .load("http://157.90.30.203/lockscreen/public/${SliderItemList[position].imageBack}")
//                .into(holder.imageBack)
//
//
//            Picasso.get()
//                .load("http://157.90.30.203/lockscreen/public/${SliderItemList[position].imageFront}")
//                .into(holder.imageFront)

            }
        }




        try {
            var typeface =
                ResourcesCompat.getFont(holder.itemView.context, SliderItemList[position].font!!)
            holder.textClock.setTypeface(typeface)
        } catch (e: Exception) {

        }

        holder.textClock.setTextColor(Color.parseColor(SliderItemList[position].color))

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)
        holder.textClock.text = "$myDate"

        holder.trash.setOnClickListener {

            onDeleteClick.invoke(position)

        }


        for (widget in SliderItemList[position].bottomWidgets) {

            val view = LayoutInflater.from(holder.widgetll.context)
                .inflate(widgetsUtility.returnViewFromName(widget), holder.widgetll, false)
            view.findViewById<CardView>(R.id.widget_remove_cv).visibility = View.GONE
            view.findViewById<CardView>(R.id.widget_back)
                .setBackgroundResource(R.drawable.icon_circle_little)

            try {
                view.findViewById<ImageView>(R.id.click_here).setOnClickListener {  }
                view.findViewById<ImageView>(R.id.click_here).isClickable = true
                view.scaleY = view.scaleY / 1.5f
                view.scaleX = view.scaleX / 1.5f
                view.layoutParams.width = 168
            } catch (e: Exception) {
                view.scaleY = view.scaleY / 1.4f
                view.scaleX = view.scaleX / 1.5f
                view.layoutParams.width = 340
            }

            holder.widgetll.addView(view)

        }

        holder.topWid.text = widgetsUtility.setTopWidgetDetails(context , SliderItemList[position].topWidget)




    }


    override fun getItemCount(): Int {

        return SliderItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var imageBack: ImageView
        lateinit var imgeEmoji : ImageView
        lateinit var videoCard: CardView
        lateinit var textClock: TextView
        lateinit var imageFront: ImageView
        lateinit var trash: CardView
        lateinit var widgetll: LinearLayout
        lateinit var topWid : TextView

        init {

            imageBack = itemView.findViewById(R.id.image_slide)
            imgeEmoji = itemView.findViewById(R.id.image_emoji_slide)
            videoCard = itemView.findViewById(R.id.video_card_cv)
            textClock = itemView.findViewById(R.id.clock_tv_slider)
            imageFront = itemView.findViewById(R.id.image_front)
            trash = itemView.findViewById(R.id.trash_back)
            widgetll = itemView.findViewById(R.id.widgets_ll_slider_my)
            topWid = itemView.findViewById(R.id.top_widget_tv_slidero)

        }


    }

    fun updateSlider(post: Int) {
        SliderItemList.clear()
        notifyDataSetChanged()
    }


}

