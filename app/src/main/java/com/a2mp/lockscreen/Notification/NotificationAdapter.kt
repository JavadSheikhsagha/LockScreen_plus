package com.a2mp.lockscreen.Notification

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a2mp.lockscreen.LockScreen.MainLockScreenData
import com.a2mp.lockscreen.R


class NotificationAdapter(
    var context: Context,
    data: MutableList<NotificationStackModel>,
    var onItemRemoveShode: (Notification) -> Unit,
    var onCategoryRemoveBeshe: (NotificationStackModel, Int) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    internal var notificationList: MutableList<NotificationStackModel>

    init {
        this.notificationList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.style_notification_stack_items, parent, false)

        return ViewHolder(layout)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (notificationList[position].notifList.size == 0) {
            return
        }


        holder.title.text = notificationList[position].notifList.first().title

        if (MainLockScreenData.getNotifSC(context) == 1) {
            holder.content.text = notificationList[position].notifList.first().content
        } else {
            holder.content.text = "Hidden content"
        }

        holder.time.text = notificationList[position].notifList.first().time


        holder.appName.text = notificationList[position].name

        try {
            holder.icon.setImageResource(notificationList[position].icon)
        } catch (e: Exception) {
            Log.i("kjasbkjasbfjkasbfjkasbfkjbasf", "onBindViewHolder: $e 2222222222222222")
        }

        if (holder.recyclerView.visibility == View.GONE) {
            when (notificationList[position].notifList.size) {
                1 -> {
                    holder.cardNumOne.visibility = View.VISIBLE
                    holder.cardNumTwo.visibility = View.GONE
                    holder.cardNumThree.visibility = View.GONE
                }
                2 -> {
                    holder.cardNumOne.visibility = View.VISIBLE
                    holder.cardNumTwo.visibility = View.VISIBLE
                    holder.cardNumThree.visibility = View.GONE
                }
                else -> {
                    holder.cardNumOne.visibility = View.VISIBLE
                    holder.cardNumTwo.visibility = View.VISIBLE
                    holder.cardNumThree.visibility = View.VISIBLE
                }
            }
        }

        if (holder.deleteCv.visibility == View.GONE){
            holder.deleteTv.visibility = View.VISIBLE
            holder.deleteIv.visibility = View.GONE
        }
        if (holder.deleteTv.visibility == View.VISIBLE){
            holder.deleteIv.visibility = View.GONE
        }


        holder.cardNumOne.setOnClickListener {

            if (holder.recyclerView.visibility == View.GONE) {
                var anim = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
                holder.recyclerView.layoutAnimation = anim
                holder.recyclerView.visibility = View.VISIBLE
                holder.cardNumTwo.visibility = View.INVISIBLE
                holder.cardNumThree.visibility = View.INVISIBLE
                holder.showLess.visibility = View.VISIBLE
                holder.appName.visibility = View.VISIBLE
                holder.deleteCv.visibility = View.VISIBLE
            }

        }

        holder.showLess.setOnClickListener {

            var anim = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
            holder.recyclerView.layoutAnimation = anim
            holder.recyclerView.visibility = View.GONE
            when (notificationList[position].notifList.size) {
                0 -> {
                    holder.cardNumTwo.visibility = View.GONE
                    holder.cardNumThree.visibility = View.GONE
                }
                1 -> {
                    holder.cardNumTwo.visibility = View.VISIBLE
                    holder.cardNumThree.visibility = View.GONE
                }
                else -> {
                    holder.cardNumTwo.visibility = View.VISIBLE
                    holder.cardNumThree.visibility = View.VISIBLE
                }
            }
            holder.showLess.visibility = View.GONE
            holder.appName.visibility = View.INVISIBLE
            holder.deleteCv.visibility = View.GONE
            holder.deleteIv.visibility = View.VISIBLE
            holder.deleteTv.visibility = View.GONE
        }

        var recycler: RecyclerView = holder.recyclerView
        try {
            var adapter =
                NotificationItemsAdapter(context, notificationList[position].notifList.apply {
                    removeAt(0)
                } ,  {
                    onItemRemoveShode.invoke(it)
                },notificationList[position].icon )


            recycler.layoutManager = LinearLayoutManager(
                holder.recyclerView.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            recycler.adapter = adapter
        } catch (e: Exception) {

        }

        holder.deleteCv.setOnClickListener {

            if (holder.deleteIv.visibility == View.VISIBLE) {
                holder.deleteIv.visibility = View.GONE
                holder.deleteTv.visibility = View.VISIBLE
            } else {
                holder.recyclerView.visibility = View.GONE
                holder.deleteIv.visibility = View.VISIBLE
                holder.deleteTv.visibility = View.GONE
                holder.deleteCv.visibility = View.GONE
                holder.showLess.visibility = View.GONE
                holder.appName.visibility = View.INVISIBLE
                holder.motion.setTransition(R.id.tran_stack_fatherooooo)
                deleteItem(position)
            }


        }


    }

    fun setItems(items: MutableList<NotificationStackModel>) {
        this.notificationList = items
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        onCategoryRemoveBeshe.invoke(notificationList[position], notificationList.size - 1)
        notificationList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return notificationList.size

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var title: TextView
        lateinit var icon: ImageView
        lateinit var content: TextView
        lateinit var time: TextView
        lateinit var recyclerView: RecyclerView
        lateinit var cardNumOne: CardView
        lateinit var cardNumTwo: CardView
        lateinit var cardNumThree: CardView
        lateinit var showLess: CardView
        lateinit var appName: TextView
        lateinit var deleteCv: CardView
        lateinit var deleteIv: ImageView
        lateinit var deleteTv: TextView
        lateinit var motion: MotionLayout

        init {

            title = itemView.findViewById(R.id.notifications_father_title)
            icon = itemView.findViewById(R.id.notification_father_icon)
            recyclerView = itemView.findViewById(R.id.notif_items_rv)
            content = itemView.findViewById(R.id.notifications_father_content)
            time = itemView.findViewById(R.id.notifications_father_time)
            cardNumOne = itemView.findViewById(R.id.card_notification_father)
            cardNumTwo = itemView.findViewById(R.id.card_notification_father_num2)
            cardNumThree = itemView.findViewById(R.id.card_notification_father_num3)
            showLess = itemView.findViewById(R.id.showless_cv)
            appName = itemView.findViewById(R.id.notification_app_name)
            deleteCv = itemView.findViewById(R.id.clear_all_cv)
            deleteIv = itemView.findViewById(R.id.clear_all_iv)
            deleteTv = itemView.findViewById(R.id.clear_all_tv)
            motion = itemView.findViewById(R.id.motion_notif_stacko)

        }


    }


}