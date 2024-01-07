package com.a2mp.lockscreen.Emoji

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "emoji_data")
class Emoji(
    @PrimaryKey(autoGenerate = false)
    var id : String,
    var name : String,
    var value : String
    ): Parcelable