package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerContractsDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerEquipmentDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerTechnicalCasesDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification

import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCustomer
import kotlinx.coroutines.launch


class CustomerVM : ViewModel() {

    private lateinit var notificationService: NotificationService
    private val hasCredentials :Boolean = true



    fun insert(context: Context, customer: Customer)
    {
        notificationService = NotificationService.getInstance(context)
        viewModelScope.launch {
            try {
                if (hasCredentials) {
                    RepoCustomer.insert(context, customer)
                    notificationService.addNotification(
                        Notification(
                            1,
                            timeStamp = customer.LastModified.toString(),
                            title = "New Customer",
                            description = "Customer ${customer.Name} has been added",
                            type = "Added",
                            function = "None",
                            seen = false)
                    )
                }
            }catch (e: SQLiteConstraintException){
                notificationService.addNotification(
                    Notification(
                        1,
                        timeStamp = customer.LastModified.toString(),
                        title = "Updated Customer Failed",
                        description = "${e.message}",
                        type = "Failed",
                        function = "None",
                        seen = false)
                )
            }
        }
        //RepoCustomer.insert(context,customer)

    }
    fun update(context: Context,customer: Customer){
        notificationService = NotificationService.getInstance(context)

        try {
            if (hasCredentials){
                viewModelScope.launch {
                    RepoCustomer.update(context,customer)
                    notificationService.addNotification(
                        Notification(
                            1,
                            timeStamp = customer.LastModified.toString(),
                            title = "Updated Customer",
                            description = "${customer.Name} information has been updated",
                            type = "Updated",
                            function = "None",
                            seen = false)
                    )
                }


            }
        }catch (e:SQLiteConstraintException){
            notificationService.addNotification(
                Notification(
                    1,
                    timeStamp = customer.LastModified.toString(),
                    title = "Updated Customer Failed",
                    description = "${e}",
                    type = "Failed",
                    function = "None",
                    seen = false)
            )
        }

    }
    fun deleteCustomer(context: Context,
                       customer: Customer,
                       onResult: (OperationResult<Unit>) -> Unit){
                           viewModelScope.launch {
                               try {
                                   if (hasCredentials) {
                                       RepoCustomer.delete(context,customer)
                                       onResult(OperationResult.Success())
                                   } else  onResult(OperationResult.Error("You don’t have permission to delete this item"))
                               }catch (e:Exception){
                                   onResult(OperationResult.Error(e.message ?: "Error deleting equipment"))
                               }
                           }


    }
    suspend fun updateSync(context: Context,customer: Customer){

        RepoCustomer.updateSync(context, customer)

    }
    suspend fun insertSync(context :Context,customer: Customer){
        RepoCustomer.insertSync(context,customer)
    }

     fun getAllCustomerData(context: Context): LiveData<List<Customer>>
    {
        return RepoCustomer.getAllCustomerData(context)
    }

    fun getCustomerDataByID(context: Context, id :String):LiveData<Customer>{
        return RepoCustomer.getCustomerID(context,id)
    }

    fun getCustomerEquipmentsPerCustomer(context: Context, id :String):LiveData<List<DashboardCustomerEquipmentDataClass>>{
        return RepoCustomer.getEquipmentDashboard(context,id)
    }
    fun getCustomerContractsPerCustomer(context: Context, id:String):LiveData<List<DashboardCustomerContractsDataClass>>{
        return RepoCustomer.getContractsDashboard(context,id)
    }
    fun getCustomerTechnicalCasesPerCustomer(context: Context, id:String):LiveData<List<DashboardCustomerTechnicalCasesDataClass>>{
        return RepoCustomer.getTechnicalCasesDashboard(context, id)
    }

}