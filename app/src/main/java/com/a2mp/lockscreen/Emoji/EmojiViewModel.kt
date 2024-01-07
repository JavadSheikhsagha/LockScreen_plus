package com.a2mp.lockscreen.Emoji

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmojiViewModel (application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Emoji>>
    val reposiroty: EmojiRepository

    init {
        val emojiDao = EmojiDatabase.getDatabase(application).emojiDao()
        reposiroty = EmojiRepository(emojiDao)
        readAllData = reposiroty.readAllData
    }

    fun addEmoji(emoji : Emoji){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.addEmoji(emoji)
        }
    }



}