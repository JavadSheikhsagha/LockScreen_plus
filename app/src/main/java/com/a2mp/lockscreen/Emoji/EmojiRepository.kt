package com.a2mp.lockscreen.Emoji

import androidx.lifecycle.LiveData
import com.a2mp.lockscreen.Notification.Notification
import com.a2mp.lockscreen.Notification.NotificationDao

class EmojiRepository(val emojiDao : EmojiDao) {

    val readAllData : LiveData<List<Emoji>> = emojiDao.readEmoji()

    suspend fun addEmoji(emoji: Emoji){
        emojiDao.addEmoji(emoji)
    }


}