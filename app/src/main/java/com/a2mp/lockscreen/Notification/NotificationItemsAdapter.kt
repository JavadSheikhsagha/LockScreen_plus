package com.a2mp.lockscreen.Notification

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.Home.WallpapersAdapter
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R
import java.lang.Exception

class NotificationItemsAdapter(
    var context: Context,
    data: MutableList<Notification>,
    var onItemRemoveShode:(Notification)-> Unit,
    var icon : Int
) : RecyclerView.Adapter<NotificationItemsAdapter.ViewHolder>() {


    internal var notificationItemsList: MutableList<Notification>

    init {
        this.notificationItemsList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_notification_simple_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.title.text = notificationItemsList[position].title
        if (MainLockScreenData.getNotifSC(context) == 1){
            holder.content.text = notificationItemsList[position].content
        }else{
            holder.content.text = "${notificationItemsList.size} Notification"
        }
        holder.time.text = notificationItemsList[position].time
        try {
            holder.icon.setImageResource(icon)
        }catch (e: Exception) {

        }

        holder.motion.jumpToState(R.id.start_swapingoo)

        holder.motion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p1 == R.id.end_swapingoo) {
                    // onSwap completed
                    // do something here

                    Log.i("LOGqwaiubfqwuirn", "onTransitionCompleted: $position")

                    deleteItem(position)


                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })


    }

    fun deleteItem(position: Int) {
        onItemRemoveShode.invoke(notificationItemsList[position])
        notificationItemsList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return notificationItemsList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var title : TextView
        lateinit var icon : ImageView
        lateinit var content : TextView
        lateinit var time : TextView
        lateinit var motion: MotionLayout

        init {

            title = itemView.findViewById(R.id.notifications_child_title)
            icon = itemView.findViewById(R.id.notification_child_icon)
            content = itemView.findViewById(R.id.notifications_child_content)
            time = itemView.findViewById(R.id.notifications_child_time)
            motion = itemView.findViewById(R.id.motion_notif_itemso)

        }


    }



}