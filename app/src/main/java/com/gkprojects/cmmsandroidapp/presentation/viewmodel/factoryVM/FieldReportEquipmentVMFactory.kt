package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportEquipmentVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoFieldReportEquipment

class FieldReportEquipmentVMFactory(
    private val repository: RepoFieldReportEquipment
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldReportEquipmentVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldReportEquipmentVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}