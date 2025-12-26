package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect

import com.gkprojects.cmmsandroidapp.data.local.dto.OverviewMainData
import com.gkprojects.cmmsandroidapp.data.local.dto.TicketCustomerName
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCases
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoUsers
import kotlinx.coroutines.launch


class CasesVM : ViewModel() { 
    private lateinit var notificationService: NotificationService
    private var hasCredentials:Boolean=true

    suspend fun insert(context: Context, cases: Tickets)
    {
        notificationService = NotificationService.getInstance(context)
        if (cases.TicketNumber.isNullOrEmpty()){
            try{
                val user= RepoUsers.getSingleUser(context,cases.UserID!!)
                val number = user.LastTCNumber?.plus(1)
                cases.TicketNumber =user.TechnicalCasePrefix + number.toString()
                RepoCases.insert(context,cases)

                user.LastTCNumber = number
                RepoUsers.updateUser(context,user)

            }catch (e:Exception){
                notificationService.addNotification(
                    Notification(1,
                        timeStamp = cases.LastModified.toString(),
                        title = "Case Error",
                        description = "$e",
                        type = "Error",
                        function = "None",
                        seen = false)
                )
            }

            }
            notificationService.addNotification(
            Notification(
            1,
            timeStamp = cases.LastModified.toString(),
            title = "New Case",
            description = "Case number ${cases.TicketNumber} created",
            type = "Added",
            function = "None",
            seen = false)
        )
    }

    fun getAllCasesData(context: Context): LiveData<List<Tickets>>
    {
        return RepoCases.getAllCustomerData(context)
    }

    suspend fun updateCase(context: Context, cases: Tickets){
        notificationService = NotificationService.getInstance(context)
        RepoCases.updateCustomerData(context,cases)
        notificationService.addNotification(
            Notification(
                1,
                timeStamp = cases.LastModified.toString(),
                title = "New Case",
                description = "Case number ${cases.TicketNumber} created",
                type = "Added",
                function = "None",
                seen = false)
        )
    }
    suspend fun getTicketDataById(context: Context,id:String?):LiveData<Tickets>{

        return RepoCases.getCustomerDataByID(context,id!!)
    }

    fun delete(context: Context,id: String? ,onResult: (OperationResult<Unit>) -> Unit){
        viewModelScope.launch {
            try{
                val cases = RepoCases.getItemByIDForDeletion(context,id!!)
                if (cases==null){
                    onResult(OperationResult.Error("Case not found"))
                    return@launch
                }
                if (cases.TicketID!=null){
                    if (hasCredentials){
                        RepoCases.delete(context,cases)
                        onResult(OperationResult.Success())
                    }else{
                        onResult(OperationResult.Error("You don’t have permission to delete this item"))
                    }

                }

            }catch (e:SQLiteConstraintException){
                onResult(OperationResult.Error(e.message!!))
                throw e
            }

        }
    }
    fun getCustomerId(context: Context): LiveData<List<CustomerSelect>> {
        return RepoCases.getCustomerIdData(context)
    }
    fun getCustomerName(context: Context) :LiveData<List<TicketCustomerName>>{
        return RepoCases.getCustomerNameTickets(context)
   }
    fun getOverviewData(context: Context):LiveData<List<OverviewMainData>>{
        return RepoCases.getDataForHome(context)
    }
    fun getAllUsers(context :Context):LiveData<List<Users>>{
        return RepoUsers.getAllUsers(context)
    }

}
