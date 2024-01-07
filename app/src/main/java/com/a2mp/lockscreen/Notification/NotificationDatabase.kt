package com.a2mp.lockscreen.Notification

import android.content.Context
import androidx.room.*
import com.a2mp.lockscreen.Database.Converters
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenDao
import com.a2mp.lockscreen.Database.LockScreenDatabase

@Database(
    entities = [Notification::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun appDao(): NotificationDao

    companion object {
        @Volatile
        var INSTANCE: NotificationDatabase? = null

        fun getDatabase(context: Context): NotificationDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}