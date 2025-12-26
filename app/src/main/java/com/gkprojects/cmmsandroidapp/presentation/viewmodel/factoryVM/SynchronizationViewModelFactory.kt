package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SynchronizationRepository

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SynchronizationViewmodel

class SynchronizationViewModelFactory (private val repository: SynchronizationRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SynchronizationViewmodel::class.java)) {
            return SynchronizationViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}