package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.gkprojects.cmmsandroidapp.presentation.ui.Contracts.ContractsVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoContracts

class ContractsViewModelFactory (private val repository: RepoContracts) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractsVM::class.java)) {
            return ContractsVM(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}