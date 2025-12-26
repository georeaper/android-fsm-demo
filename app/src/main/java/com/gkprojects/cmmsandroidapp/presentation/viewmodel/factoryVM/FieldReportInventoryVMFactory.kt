package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportInventoryVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderInventory

class FieldReportInventoryVMFactory (
    private val repository: RepoWorkOrderInventory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldReportInventoryVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldReportInventoryVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}