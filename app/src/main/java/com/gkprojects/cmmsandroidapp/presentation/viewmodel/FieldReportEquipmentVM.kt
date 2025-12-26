package com.gkprojects.cmmsandroidapp.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomDisplayDatFieldReportEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoFieldReportEquipment
import kotlinx.coroutines.launch

class FieldReportEquipmentVM(private val repository: RepoFieldReportEquipment) : ViewModel() {



    fun loadSingleReport(id: String):LiveData<FieldReportEquipment> {

        return repository.getFieldReportByID(id)

    }
    fun loadAllEquipmentsByReportID(id: String):LiveData<List<FieldReportEquipment>> {
        return repository.getAllFieldReportID(id)
    }

    fun loadCustomDisplay(id: String):LiveData<List<CustomDisplayDatFieldReportEquipments>> {

        return repository.getFieldReportEquipmentByID(id)

    }

    fun insert(fieldReportEquipment: FieldReportEquipment) {
        viewModelScope.launch {

           repository.insertFieldReportEquipment(fieldReportEquipment)

        }
    }

    fun update(fieldReportEquipment: FieldReportEquipment) {
        viewModelScope.launch {

           repository.updateFieldReportEquipment(fieldReportEquipment)

        }
    }

    fun updateStatus(id: String, value: Int) {
        viewModelScope.launch {

            repository.updateStatusFieldReportEquipmentByID(value, id)

        }
    }

    fun delete(fieldReportEquipment: FieldReportEquipment) {
        viewModelScope.launch {

            repository.deleteFieldReportEquipment(fieldReportEquipment)

        }
    }

}