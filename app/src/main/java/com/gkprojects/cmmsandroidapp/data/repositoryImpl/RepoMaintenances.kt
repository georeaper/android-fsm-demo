package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoMaintenances {
    companion object {
        var userDatabase: CMMSDatabase? = null

        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }

        fun insert(context: Context,maintenances: Maintenances)
        {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            maintenances.DateCreated = now
            maintenances.LastModified= now
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.MaintenancesDao().addMaintenances(maintenances)
            }
        }
        suspend fun delete(context: Context,maintenances: Maintenances){
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch{
                userDatabase!!.MaintenancesDao().deleteMaintenances(maintenances)
            }
        }
        fun getAllMaintenances(context: Context): LiveData<List<Maintenances>>
        {
            userDatabase = initialiseDB(context)
            return userDatabase!!.MaintenancesDao().getAllMaintenances()
        }
        fun update(context: Context,maintenances: Maintenances){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            maintenances.LastModified= now
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.MaintenancesDao().updateMaintenances(maintenances)
            }
        }

        suspend fun getAllListForSync(context: Context): List<Maintenances> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.MaintenancesDao().getAllMaintenancesList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, list: List<Maintenances>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = userDatabase!!.MaintenancesDao().getMaintenancesByIDNow(it.MaintenanceID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.MaintenancesDao().addMaintenances(it)
                    } else {
                        userDatabase!!.MaintenancesDao().updateMaintenances(it)
                    }
                }
            }
        }
    }

}