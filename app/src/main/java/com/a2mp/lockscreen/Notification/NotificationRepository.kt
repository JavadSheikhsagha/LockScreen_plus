package com.a2mp.lockscreen.Notification

import androidx.lifecycle.LiveData
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenDao

class NotificationRepository (val notificationDao : NotificationDao){

    val readAllData : LiveData<List<Notification>> = notificationDao.readNotification()

    suspend fun addNotif(notification: Notification){
        notificationDao.addNotification(notification)
    }

    suspend fun updateNotif(notification: Notification){
        notificationDao.updateNotification(notification)
    }

    suspend fun deleteNotif(notification: Notification){
        notificationDao.deleteNotification(notification)
    }


}