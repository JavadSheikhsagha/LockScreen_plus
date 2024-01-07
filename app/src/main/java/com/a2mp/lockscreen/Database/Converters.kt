package com.a2mp.lockscreen.Database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.a2mp.lockscreen.Widgets.WidgetModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class Converters {

    @TypeConverter
    fun fromString(value: String?) : ArrayList<String>{

        var listType  = object : TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value , listType)
    }

    @TypeConverter
    fun fromArraylist(list: List<String?>): String {

        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap (byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray , 0  , byteArray.size)
    }



//    @TypeConverter
//    fun fromListIntToString(intList: List<Int>): String = intList.toString()
//    @TypeConverter
//    fun toListIntFromString(stringList: String): List<Int> {
//        val result = ArrayList<Int>()
//        val split =stringList.replace("[","").replace("]","").replace(" ","").split(",")
//        for (n in split) {
//            try {
//                result.add(n.toInt())
//            } catch (e: Exception) {
//
//            }
//        }
//        return result
//    }



}