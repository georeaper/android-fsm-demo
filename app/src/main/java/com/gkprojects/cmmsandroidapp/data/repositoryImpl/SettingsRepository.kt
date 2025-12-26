package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.SettingsDao
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings as AppSettings

class SettingsRepository private constructor(context: Context) {

    private val settingsDao: SettingsDao = CMMSDatabase.getInstance(context)!!.SettingsDao()

    companion object {
        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            return instance ?: synchronized(this) {
                instance ?: SettingsRepository(context).also { instance = it }
            }
        }
    }

    // Example method to insert settings
    suspend fun insertSettings(settings: AppSettings): Long {
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        settings.DateCreated = now
        settings.LastModified=now
        return settingsDao.insertSettings(settings)
    }

    // Example method to retrieve settings by key
    suspend fun getSettingsByKey(key: String): List<AppSettings> {
        return settingsDao.getSettingsByKey(key)
    }
    suspend fun deleteSettings(settings: Settings):Int{
        return settingsDao.deleteSettings(settings)
    }
    suspend fun updateSettings(settings: Settings):Int{
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        settings.LastModified=now
        return settingsDao.updateSettings(settings)
    }

    suspend fun getAllListForSync(): List<Settings> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            settingsDao.getAllAppSettingsList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate(lists: List<Settings>)  {

        withContext(Dispatchers.IO) {
            lists.forEach {
                val existing = settingsDao.getAppSettingsByIDNow(it.SettingsID)  // This should be suspend
                if (existing == null) {
                    settingsDao.insertSettings(it)
                } else {
                    settingsDao.updateSettings(it)
                }
            }
        }
    }
}
