package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.SynchronizationDao
import com.gkprojects.cmmsandroidapp.data.local.entity.Synchronization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SynchronizationRepository private constructor(context: Context) {
    private val synchronizationDao : SynchronizationDao = CMMSDatabase.getInstance(context)!!.SynchronizationDao()

    companion object{
        @Volatile
        private var instance: SynchronizationRepository? = null

        fun getInstance(context: Context): SynchronizationRepository {
            return instance ?: synchronized(this) {
                instance ?: SynchronizationRepository(context).also { instance = it }
            }
        }
    }
    suspend fun insert(synchronization: Synchronization)
    {
//        val currentDateTime = Calendar.getInstance().time
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        synchronization.LastSyncDate= now
        synchronizationDao.insert(synchronization)

    }
    suspend fun delete(synchronization: Synchronization){
            synchronizationDao.delete(synchronization)
    }

    suspend fun getLastSyncDate(entity :String): Synchronization {
        return synchronizationDao.getLastSyncDate(entity)
    }

    suspend fun getAllListForSync(): List<Synchronization> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            synchronizationDao.getAllSynchronizationList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate(lists: List<Synchronization>)  {

        withContext(Dispatchers.IO) {
            lists.forEach {
                val existing = synchronizationDao.getSynchronizationByIDNow(it.SyncID)  // This should be suspend
                if (existing == null) {
                    synchronizationDao.insert(it)
                } else {
                    synchronizationDao.update(it)
                }
            }
        }
    }


}