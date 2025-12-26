package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportToolsDao
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoWorkOrderTools private constructor(context: Context) {

    private val dao : FieldReportToolsDao = CMMSDatabase.getInstance(context)!!.FieldReportToolsDao()

    companion object {
        @Volatile
        private var instance: RepoWorkOrderTools? = null

        fun getInstance(context: Context): RepoWorkOrderTools {
            return instance ?: synchronized(this) {
                instance ?: RepoWorkOrderTools(context).also { instance = it }
            }
        }
    }

        suspend fun insertWorkOrderTools(fieldReportTools: FieldReportTools){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            fieldReportTools.DateCreated = now
            fieldReportTools.LastModified= now
            dao.insertFieldReportTool(fieldReportTools)
        }

        suspend fun updateWorkOrderTools(fieldReportTools: FieldReportTools){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            fieldReportTools.LastModified=now
            dao.updateFieldReportTools(fieldReportTools)
        }
        suspend fun deleteWorkOrderTools(fieldReportTools: FieldReportTools){
            dao.deleteFieldReportTool(fieldReportTools)
        }
        fun getWorkOrderToolsByReportID(id :String):LiveData<List<FieldReportToolsCustomData>>{

            return dao.getFieldReportToolsByID(id)
        }
        suspend fun getAllListForSync(): List<FieldReportTools>  {

            return withContext(Dispatchers.IO) {
                dao.getAllFieldReportToolsList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate( list: List<FieldReportTools>)  {

            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = dao.getFieldReportToolsByIDNow(it.FieldReportToolsID)// This should be suspend
                    if (existing == null) {
                        dao.insertFieldReportTool(it)
                    } else {
                        dao.updateFieldReportTools(it)
                    }
                }
            }
        }



}