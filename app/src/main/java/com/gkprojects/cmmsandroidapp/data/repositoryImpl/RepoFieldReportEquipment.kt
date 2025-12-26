package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportEquipmentDao
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomDisplayDatFieldReportEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoFieldReportEquipment private constructor(context: Context) {

    private val dao : FieldReportEquipmentDao = CMMSDatabase.getInstance(context)!!.FieldReportEquipmentDao()

    companion object {
        @Volatile
        private var instance: RepoFieldReportEquipment? = null

        fun getInstance(context: Context): RepoFieldReportEquipment {
            return instance ?: synchronized(this) {
                instance ?: RepoFieldReportEquipment(context).also { instance = it }
            }
        }
    }


    suspend fun insertFieldReportEquipment(fieldReportEquipment: FieldReportEquipment){
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        fieldReportEquipment.DateCreated = now
        fieldReportEquipment.LastModified= now
        dao.addFieldReportEquipment(fieldReportEquipment)
    }

    suspend fun deleteFieldReportEquipment(fieldReportEquipment: FieldReportEquipment){
        dao.deleteFieldReportEquipment(fieldReportEquipment)

    }

    suspend fun updateFieldReportEquipment(fieldReportEquipment: FieldReportEquipment){
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)

        fieldReportEquipment.LastModified=now
        dao.updateFieldReportEquipment(fieldReportEquipment)
    }

    fun getFieldReportEquipmentByID(id : String) :LiveData<List<CustomDisplayDatFieldReportEquipments>>{

        return dao.getFieldReportEquipmentByFieldReportID(id)

    }

    fun getFieldReportByID(id: String):LiveData<FieldReportEquipment>{

            return dao.getFieldReportEquipmentByID(id)

    }

    suspend fun updateStatusFieldReportEquipmentByID(value :Int,id:String){
        dao.updateCompletedStatus(value,id)
    }

    suspend fun getAllListForSync(): List<FieldReportEquipment> {

            return withContext(Dispatchers.IO) {
                dao.getAllFieldReportEquipmentList()
            }
        }

    suspend fun insertOrUpdate(list: List<FieldReportEquipment>)  {

            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing =dao.getFieldReportEquipmentByIDNow(it.FieldReportEquipmentID)// This should be suspend
                    if (existing == null) {
                        dao.addFieldReportEquipment(it)
                    } else {
                        dao.updateFieldReportEquipment(it)
                    }
                }
            }
        }

    fun getAllFieldReportID(id: String): LiveData<List<FieldReportEquipment>> {
        return dao.getAllFieldReportEquipmentByReportID(id)

    }
}

