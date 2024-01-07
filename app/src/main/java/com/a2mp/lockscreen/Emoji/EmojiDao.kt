package com.a2mp.lockscreen.Emoji

import androidx.lifecycle.LiveData
import androidx.room.*
import com.a2mp.lockscreen.Notification.Notification

@Dao
interface EmojiDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEmoji(emoji: Emoji)

    @Query("SELECT * FROM emoji_data ORDER BY id ASC")
    fun readEmoji(): LiveData<List<Emoji>>



}