package com.a2mp.lockscreen.Notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.a2mp.lockscreen.Database.LockScreen
import com.a2mp.lockscreen.Database.LockScreenDatabase
import com.a2mp.lockscreen.Database.LockScreenReposiroty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationViewModel (application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Notification>>
    val reposiroty: NotificationRepository
    val appDao : NotificationDao

    init {
        appDao = NotificationDatabase.getDatabase(application).appDao()
        reposiroty = NotificationRepository(appDao)
        readAllData = reposiroty.readAllData

    }

    fun addNotification(notification : Notification){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.addNotif(notification)
        }
    }

    fun updateNotification(notification: Notification){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateNotif(notification)
        }
    }

    fun deleteNotification(notification: Notification){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.deleteNotif(notification)
        }
    }

    fun getNotificationByPackageName(packageName : String): MutableList<Notification> {
        return  appDao.getNotificationsByPackageName(packageName)

    }

    fun getUniquePackageName(): MutableList<String> {
        return appDao.getUniquePackageNames()
    }


    fun deleteAllByPackageName(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteAllByPackageName(packageName)
        }
    }

    fun deleteAllNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteAll()
        }
    }

}