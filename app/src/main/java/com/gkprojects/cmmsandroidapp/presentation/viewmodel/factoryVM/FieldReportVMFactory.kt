package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.WorkOrdersVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders

class FieldReportVMFactory (private val repository: RepoWorkOrders
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkOrdersVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkOrdersVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}