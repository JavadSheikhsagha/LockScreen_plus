package com.a2mp.lockscreen.Database

import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LockScreen::class],
    version = 5 ,
    autoMigrations = [
        AutoMigration(from = 4  , to = 5)
    ], exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LockScreenDatabase : RoomDatabase() {

    abstract fun appDao(): LockScreenDao

    companion object {
        @Volatile
        var INSTANCE: LockScreenDatabase? = null

        fun getDatabase(context: Context): LockScreenDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LockScreenDatabase::class.java,
                    "lockscreen_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }




    }



}