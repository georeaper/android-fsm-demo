package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.ui.Contracts.ContractEquipmentViewModel
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepositoryContractEquipment

class ContractEquipmentViewModelFactory (private val repository: RepositoryContractEquipment) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractEquipmentViewModel::class.java)) {
            return ContractEquipmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}