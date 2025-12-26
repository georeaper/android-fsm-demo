package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.gkprojects.cmmsandroidapp.data.local.entity.Settings as AppSettings

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: AppSettings): Long

    @Update
    suspend fun updateSettings(settings: AppSettings): Int

    @Query("Select * from Settings")
     fun getAllSettings(): LiveData<List<AppSettings>>

    @Query("Select * from Settings where SettingsKey= :key ")
    suspend fun getSettingsByKey(key:String):List<AppSettings>

   @Delete
    suspend fun deleteSettings(settings: AppSettings):Int

    @Query("SELECT * FROM Settings")
    suspend fun getAllAppSettingsList(): List<AppSettings> // <--- For sync

    @Query("SELECT * FROM Settings WHERE SettingsID = :id LIMIT 1")
    suspend fun getAppSettingsByIDNow(id: String): AppSettings? // <--- For sync

}