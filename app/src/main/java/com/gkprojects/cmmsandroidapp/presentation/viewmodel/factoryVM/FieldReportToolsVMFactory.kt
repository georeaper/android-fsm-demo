package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportToolsVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderTools

class FieldReportToolsVMFactory (
    private val repository: RepoWorkOrderTools
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldReportToolsVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldReportToolsVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}