package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportInventoryDao
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoWorkOrderInventory private constructor(context: Context) {
    private val dao : FieldReportInventoryDao = CMMSDatabase.getInstance(context)!!.FieldReportInventoryDao()
    companion object {
        @Volatile
        private var instance: RepoWorkOrderInventory? = null

        fun getInstance(context: Context): RepoWorkOrderInventory {
            return instance ?: synchronized(this) {
                instance ?: RepoWorkOrderInventory(context).also { instance = it }
            }
        }
    }

        suspend fun insertWorkOrderInventory( fieldReportInventory: FieldReportInventory){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            fieldReportInventory.DateCreated = now
            fieldReportInventory.LastModified= now
            dao.addFieldReportInventory(fieldReportInventory)
        }
        suspend fun updateWorkOrderInventory( fieldReportInventory: FieldReportInventory){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            fieldReportInventory.LastModified=now
            dao.updateFieldReportInventory(fieldReportInventory)
        }
        suspend fun deleteWorkOrderInventory( fieldReportInventory: FieldReportInventory){
            dao.deleteFieldReportInventory(fieldReportInventory)
        }

        fun getWorkOrderInventoryByReportID(id :String):LiveData<List<FieldReportInventoryCustomData>>{

            return dao.getFieldReportInventoryByID(id)
        }

        suspend fun getAllListForSync(): List<FieldReportInventory> {
            return withContext(Dispatchers.IO) {
                dao.getAllFieldReportInventoryList()// This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(list: List<FieldReportInventory>)  {

            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = dao.getFieldReportInventoryByIDNow(it.FieldReportInventoryID)// This should be suspend
                    if (existing == null) {
                        dao.addFieldReportInventory(it)
                    } else {
                        dao.updateFieldReportInventory(it)
                    }
                }
            }
        }

}