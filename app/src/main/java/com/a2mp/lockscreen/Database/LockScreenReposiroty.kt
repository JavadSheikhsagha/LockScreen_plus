package com.a2mp.lockscreen.Database

import androidx.lifecycle.LiveData

class LockScreenReposiroty (val lockScreenDao : LockScreenDao){

    val readAllData : LiveData<List<LockScreen>> = lockScreenDao.readLockScreen()

    suspend fun addApp(lockScreen: LockScreen){
        lockScreenDao.addLockScreen(lockScreen)
    }

    suspend fun updateApp(lockScreen: LockScreen){
        lockScreenDao.updateLockScreen(lockScreen)
    }

    suspend fun deleteApp(lockScreen: LockScreen){
        lockScreenDao.deleteLockScreen(lockScreen)
    }

}