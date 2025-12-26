package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportCheckListVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckListItems


class FieldReportChecklistVMFactory (
    private val repository: RepoCheckListItems
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldReportCheckListVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FieldReportCheckListVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}