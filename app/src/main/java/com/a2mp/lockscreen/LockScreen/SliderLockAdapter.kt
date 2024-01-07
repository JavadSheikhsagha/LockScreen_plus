package com.a2mp.lockscreen.LockScreen


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
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
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class SliderLockAdapter(
    var context: Context,
    data: MutableList<LockScreen>,
    viewPager2: ViewPager2,
    var onItemClick: (LockScreen) -> Unit
) : RecyclerView.Adapter<SliderLockAdapter.ViewHolder>() {

    internal var SliderLockItemList: MutableList<LockScreen>
    var widgetsUtility = WidgetsUtility()

    init {
        this.SliderLockItemList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_slider_lock_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (SliderLockItemList[position].isEmoje == true){
            holder.imageBack.setBackgroundColor(Color.parseColor(SliderLockItemList[position].backColor!!))
            holder.imageEmoji.setImageBitmap(BitmapFactory.decodeByteArray(SliderLockItemList[position].emoji, 0, SliderLockItemList[position].emoji!!.size))
        }else{
            if (SliderLockItemList[position].imageBack.contains("/")) {

                holder.imageBack.setImageURI(Uri.parse(SliderLockItemList[position].imageBack))
                holder.imageFront.setImageURI(Uri.parse(SliderLockItemList[position].imageFront))

            } else {
                Picasso.get()
                    .load("http://157.90.30.203/lockscreen/public/${SliderLockItemList[position].imageBack}")
                    .into(holder.imageBack)


                Picasso.get()
                    .load("http://157.90.30.203/lockscreen/public/${SliderLockItemList[position].imageFront}")
                    .into(holder.imageFront)
            }
        }



        try {
            val typeface =
                ResourcesCompat.getFont(holder.itemView.context, SliderLockItemList[position].font)
            holder.textClock.setTypeface(typeface)
        }catch (e : Exception){

        }

        holder.textClock.setTextColor(Color.parseColor(SliderLockItemList[position].color))

        var date = Date()
        val formatter = SimpleDateFormat("HH:mm")
        var myDate: String = formatter.format(date)
        holder.textClock.text = "$myDate"

        holder.videoCard.setOnClickListener {

            onItemClick.invoke(SliderLockItemList[position])

        }

        for (widget in SliderLockItemList[position].bottomWidgets) {

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

        holder.topWid.text = widgetsUtility.setTopWidgetDetails(context , SliderLockItemList[position].topWidget)



    }


    override fun getItemCount(): Int {

        return SliderLockItemList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        lateinit var imageBack: ImageView
        lateinit var imageEmoji : ImageView
        lateinit var videoCard: CardView
        lateinit var textClock: TextView
        lateinit var imageFront: ImageView
        lateinit var widgetll : LinearLayout
        lateinit var topWid : TextView

        init {

            imageBack = itemView.findViewById(R.id.image_slide)
            imageEmoji = itemView.findViewById(R.id.image_emoji_slide)
            videoCard = itemView.findViewById(R.id.video_card_cv)
            textClock = itemView.findViewById(R.id.clock_tv_slider)
            imageFront = itemView.findViewById(R.id.image_front)
            widgetll = itemView.findViewById(R.id.widgets_ll_slider_lock)
            topWid = itemView.findViewById(R.id.top_widget_tv_change_lock)

        }


    }


}

