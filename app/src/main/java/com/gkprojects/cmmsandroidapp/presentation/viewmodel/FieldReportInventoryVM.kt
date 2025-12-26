package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderInventory
import kotlinx.coroutines.launch


class FieldReportInventoryVM(private val repository: RepoWorkOrderInventory) : ViewModel(){

    fun insert (fieldReportInventory: FieldReportInventory){
        viewModelScope.launch {
            repository.insertWorkOrderInventory(fieldReportInventory)
        }

    }

    fun delete(fieldReportInventory: FieldReportInventory){
        viewModelScope.launch {
            repository.deleteWorkOrderInventory(fieldReportInventory)

        }
        //repository.deleteWorkOrderInventory( fieldReportInventory)
    }

    fun getInventoryByFieldReportID( id :String ):LiveData<List<FieldReportInventoryCustomData>> {

        return repository.getWorkOrderInventoryByReportID(id)

    }
}