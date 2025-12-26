package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ManufacturerRepository

class ManufacturerViewModelFactory (private val repository: ManufacturerRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManufacturerViewModel::class.java)) {
            return ManufacturerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


