package com.a2mp.lockscreen.Emoji

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.a2mp.lockscreen.Database.Converters
import com.a2mp.lockscreen.Notification.Notification
import com.a2mp.lockscreen.Notification.NotificationDao
import com.a2mp.lockscreen.Notification.NotificationDatabase

@Database(
    entities = [Emoji::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EmojiDatabase : RoomDatabase() {


    abstract fun emojiDao(): EmojiDao

    companion object {
        @Volatile
        var INSTANCE: EmojiDatabase? = null

        fun getDatabase(context: Context): EmojiDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmojiDatabase::class.java,
                    "emoji_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }



}