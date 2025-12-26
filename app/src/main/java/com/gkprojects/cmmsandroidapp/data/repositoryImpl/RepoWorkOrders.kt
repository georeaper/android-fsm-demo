package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportsDao
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.local.dto.WorkOrdersList
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoWorkOrders private constructor(context: Context) {
    private val dao : FieldReportsDao = CMMSDatabase.getInstance(context)!!.FieldReportsDao()
    companion object {
        @Volatile
        private var instance: RepoWorkOrders? = null

        fun getInstance(context: Context): RepoWorkOrders {
            return instance ?: synchronized(this) {
                instance ?: RepoWorkOrders(context).also { instance = it }
            }
        }
    }

        suspend fun insert(workOrder : FieldReports)
        {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            workOrder.DateCreated = now
            workOrder.LastModified= now
            dao.addFieldReports(workOrder)

        }
       suspend fun update(workOrder: FieldReports){
           val currentDateTime = Calendar.getInstance().time
           val now = DateUtils.format(currentDateTime)

            workOrder.LastModified= now
            dao.updateFieldReports(workOrder)

        }
        suspend fun delete(workOrder: FieldReports){
            dao.deleteFieldReports(workOrder)
        }
        suspend fun getWorkOrderByIDForDeletion(id: String):FieldReports?{
            return dao.getFieldReportsByIDNow(id)
        }

        fun getWorkOrderByID(id :String):LiveData<FieldReports>{

            return dao.getReportsByID(id)
        }

        fun getWorkOrdersCustomerName():LiveData<List<WorkOrdersList>>{

            return dao.getCustomerName()
        }
        fun printPdfCustomerData(id: String):LiveData<CustomWorkOrderPDFDATA>{

            return dao.printDetails(id)
        }
        fun getEquipmentListAndChecklistByReportID(id: String):LiveData<List<CustomCheckListWithEquipmentData>>{

            return dao.printEquipmentWithCheckList(id)
        }

        fun printToolsByReportID(id: String):LiveData<List<FieldReportToolsCustomData>>{

            return dao.printToolsByReportID(id)
        }
        fun printInventoryReportID(id: String):LiveData<List<FieldReportInventoryCustomData>>{

            return dao.printInventoryDataByReportID(id)
        }

        suspend fun getAllListForSync( ): List<FieldReports> {

            return withContext(Dispatchers.IO) {
                dao.getAllFieldReportsList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate( list: List<FieldReports>)  {

            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = dao.getFieldReportsByIDNow(it.FieldReportID)// This should be suspend
                    if (existing == null) {
                        dao.addFieldReports(it)
                    } else {
                        dao.updateFieldReports(it)
                    }
                }
            }
        }

        suspend fun getUserByIDworkOrder(id :String): Users {

            return dao.getUserByID(id)

        }

        suspend fun updateReportNumber(number : Int ,id : String){
            dao.increaseLastReportNumber(number,id)

        }
}