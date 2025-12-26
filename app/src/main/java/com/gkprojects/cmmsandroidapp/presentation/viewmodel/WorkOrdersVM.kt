package com.gkprojects.cmmsandroidapp.presentation.viewmodel


import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult

import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.dto.WorkOrdersList
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders
import kotlinx.coroutines.launch


class WorkOrdersVM(private val repository: RepoWorkOrders) :ViewModel() {

    private val hasCredentials :Boolean = true

    fun insert( workOrder: FieldReports)
    {
        viewModelScope.launch {
            Log.d("testWorkOrderVMStartInsert","$workOrder")
            if (workOrder.UserID!=null){
                val user = repository.getUserByIDworkOrder(workOrder.UserID!!)

                var tempNumber = user.LastReportNumber
                if (tempNumber != null) {
                    tempNumber += 1
                }
                Log.d("last2","$tempNumber")
                val formattedNumber = formatNumber(tempNumber, 8)

                val reportNumber = user.ReportPrefix + formattedNumber
                workOrder.ReportNumber=reportNumber

                repository.insert(workOrder)
                Log.d("testWorkOrderVM","$workOrder")
                repository.updateReportNumber(tempNumber!!,user.UserID)
                Log.d("testWorkOrderVM","$tempNumber ${user.UserID}")

            }


        }


    }

    fun update(workOrder: FieldReports){
        viewModelScope.launch {
            Log.d("testWorkOrderVM","$workOrder")
            repository.update(workOrder)
        }

    }
    fun delete(id:String,
               onResult: (OperationResult<Unit>) -> Unit){
        viewModelScope.launch {
            try {
                val workOrder = repository.getWorkOrderByIDForDeletion(id)
                if (workOrder==null){
                    onResult(OperationResult.Error("Work order not found"))
                    return@launch
                }
                if (hasCredentials) {
                repository.delete(workOrder)
                onResult(OperationResult.Success())
            } else {
                onResult(OperationResult.Error("You don’t have permission to delete this item"))
            }

            } catch (e: SQLiteConstraintException) {
                throw e
            }
        }

    }
    fun getWorkOrderByID(id :String):LiveData<FieldReports>{
        return repository.getWorkOrderByID( id)
    }
    fun getWorkOrdersCustomerName():LiveData<List<WorkOrdersList>>{
        return repository.getWorkOrdersCustomerName()
    }
    fun printWorkOrder (id: String):LiveData<CustomWorkOrderPDFDATA>{
        return repository.printPdfCustomerData( id)
    }
    fun getDataEquipmentListAndCheckListByReportID(id: String):LiveData<List<CustomCheckListWithEquipmentData>>{
        return repository.getEquipmentListAndChecklistByReportID( id)
    }
    fun printDataInventoryListByReportID(id: String):LiveData<List<FieldReportInventoryCustomData>>{
        return repository.printInventoryReportID(id)
    }
    fun printDataToolsListByReportID(id: String):LiveData<List<FieldReportToolsCustomData>>{
        return repository.printToolsByReportID( id)
    }
    private fun formatNumber(number: Int?, digits: Int): String {
        return String.format("%0${digits}d", number)
    }
}