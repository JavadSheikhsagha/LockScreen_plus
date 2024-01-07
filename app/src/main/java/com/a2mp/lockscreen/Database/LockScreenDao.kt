package com.a2mp.lockscreen.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LockScreenDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLockScreen(lockScreen: LockScreen)

    @Query("SELECT * FROM lockscreen_data ORDER BY id ASC")
    fun readLockScreen(): LiveData<List<LockScreen>>

    @Update
    fun updateLockScreen(lockScreen: LockScreen)

    @Delete
    fun deleteLockScreen(lockScreen: LockScreen)


}