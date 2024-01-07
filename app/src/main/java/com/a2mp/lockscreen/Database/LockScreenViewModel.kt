package com.a2mp.lockscreen.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockScreenViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<LockScreen>>
    val reposiroty: LockScreenReposiroty

    init {
        val appDao = LockScreenDatabase.getDatabase(application).appDao()
        reposiroty = LockScreenReposiroty(appDao)
        readAllData = reposiroty.readAllData
    }

    fun addLockscreen(lockScreen : LockScreen ){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.addApp(lockScreen)
        }
    }

    fun updateLockscreen(lockScreen: LockScreen){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateApp(lockScreen)
        }
    }

    fun deleteLockscreen(lockScreen: LockScreen){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.deleteApp(lockScreen)
        }
    }

}