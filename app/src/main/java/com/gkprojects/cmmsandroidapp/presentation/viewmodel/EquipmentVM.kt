package com.gkprojects.cmmsandroidapp.presentation.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentListInCases
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName

import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets



import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment
import kotlinx.coroutines.launch

class EquipmentVM(private val repo: RepoEquipment) :ViewModel() {

    //get credentials for user from prefsharedcredentials

    //private val hasCredentials:Boolean =getCredentialsUsers().deleteEquipment()
    private val hasCredentials :Boolean = true

//    private fun hasCredentials(context: Context): Boolean {
//        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//        return prefs.getBoolean("canDeleteEquipment", false)
//    }


    fun insertEquipment(equipment: Equipments, onInserted: (Equipments) -> Unit) {
        viewModelScope.launch {
            repo.insert(equipment)
            onInserted(equipment) // callback for Fragment to trigger notification
        }
    }

    fun updateEquipment(equipment: Equipments, onUpdated: (Equipments) -> Unit) {
        viewModelScope.launch {
            repo.updateEquipmentData(equipment)
            onUpdated(equipment) // callback for Fragment to trigger notification
        }
    }
    fun deleteEquipmentById(
        context: Context,
        id: String,
        onResult: (OperationResult<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val equipment = repo.getRecordById(id)
                if (equipment != null) {
                    //if (hasCredentials(context)) {
                    if (hasCredentials) {
                        repo.delete(equipment)
                        onResult(OperationResult.Success())
                    } else {
                        onResult(OperationResult.Error("You don’t have permission to delete this item"))
                    }
                } else {
                    onResult(OperationResult.Error("Record not found"))
                }
            } catch (e: Exception) {
                onResult(OperationResult.Error(e.message ?: "Error deleting equipment"))
            }
        }
    }


    fun deleteEquipment(context: Context, equipment: Equipments, onResult: (OperationResult<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                //if (hasCredentials(context)) {
                if (hasCredentials) {
                    repo.delete(equipment)
                    onResult(OperationResult.Success())
                } else {
                    onResult(OperationResult.Error("You don’t have permission to delete this item"))
                }
            } catch (e: Exception) {
                onResult(OperationResult.Error(e.message ?: "Unknown error"))
            }
        }
    }

    //One time call , not continuous,dialog recycler view to select equipment
    fun getAllEquipmentDataByCustomerID(customerId: String, onResult: (List<Equipments>) -> Unit) {
        viewModelScope.launch {
            val list = repo.getEquipmentsDataByCustomerID(customerId)
            onResult(list)
        }
    }

    // ✅ Continuous call (live observable data)
    // Used to observe all equipments and auto-update UI
    fun getAllEquipmentData(): LiveData<List<Equipments>> {
        return repo.getAllEquipmentData()
    }

    //One time call , not continuous
    suspend fun getTicketByEquipmentId(id: String, onResult: (List<Tickets>) -> Unit) {

        viewModelScope.launch {
            val list = repo.getTicketsByEquipmentId(id)
            onResult(list)
        }
    }

    //One time call , not continuous
    fun getCustomerId(onResult: (List<CustomerSelect>) -> Unit) {
        viewModelScope.launch {
            val list = repo.getCustomerID()
            onResult(list)
        }

    }

    //One time call , not continuous
    fun getCustomerName(onResult: (List<EquipmentSelectCustomerName>) -> Unit) {
        viewModelScope.launch {
            val list = repo.getCustomerNameDashboard()
            onResult(list)
        }
        //}):LiveData<List<EquipmentSelectCustomerName>>{
//        return repo.getCustomerNameDashboard()
    }

    //One time call , not continuous
    fun getRecordById(id: String, onResult: (Equipments) -> Unit) {
        viewModelScope.launch {
            val list = repo.getRecordById(id)
            onResult(list)
        }
    }

    //One time call , not continuous
    fun getEquipmentByCustomerId(
        id: String,
        onResult: (List<EquipmentListInCases>) -> Unit
    ) {
        viewModelScope.launch {
            val list = repo.getEquipmentByCustomer(id)
            onResult(list)
        }
    }


}
