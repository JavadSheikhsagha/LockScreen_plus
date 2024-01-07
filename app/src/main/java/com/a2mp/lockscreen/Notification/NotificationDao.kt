package com.a2mp.lockscreen.Notification

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNotification(notification: Notification)

    @Query("SELECT * FROM notification_data ORDER BY id ASC")
    fun readNotification(): LiveData<List<Notification>>

    @Update
    fun updateNotification(notification: Notification)

    @Delete
    fun deleteNotification(notification: Notification)

    @Query("SELECT * FROM notification_data WHERE packageName = :packageNames ORDER BY id ASC")
    fun getNotificationsByPackageName(packageNames: String): MutableList<Notification>

    @Query("SELECT packageName FROM notification_data GROUP BY packageName ORDER BY id ASC")
    fun getUniquePackageNames(): MutableList<String>

    @Query("DELETE FROM notification_data WHERE packageName = :packageNames")
    fun deleteAllByPackageName(packageNames: String)

    @Query("DELETE FROM notification_data")
    fun deleteAll()

}