package com.a2mp.lockscreen.Notification

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notification_data")
class Notification(
    @PrimaryKey(autoGenerate = false)
    var id : String,
    var appName : String,
    var packageName: String,
    var title: String?,
    var content: String?,
    var time: String
) : Parcelable