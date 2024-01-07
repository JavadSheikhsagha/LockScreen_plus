package com.a2mp.lockscreen.Database



import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.a2mp.lockscreen.Widgets.TopWidgetItems
import com.a2mp.lockscreen.Widgets.WidgetModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@Entity(tableName = "lockscreen_data")
data class LockScreen(
    @PrimaryKey(autoGenerate = false)
    var id : Long,
    var color: String,
    var font : Int,
    var imageBack : String,
    var imageFront : String,
    @ColumnInfo(name = "bottomWidgets"  , defaultValue = "0")
    var bottomWidgets :  MutableList<String>,
    @ColumnInfo(name = "topWidget"  , defaultValue = "Date")
    var topWidget :  String,
    @ColumnInfo(name = "isEmoje"  , defaultValue = "true")
    var isEmoje : Boolean? = false,
    @ColumnInfo(name = "emoji"  , defaultValue = "")
    var emoji : ByteArray?  = byteArrayOf(),
    @ColumnInfo(name = "backColor"  , defaultValue = "#FFFFFF")
    var backColor : String? = ""
) : Parcelable