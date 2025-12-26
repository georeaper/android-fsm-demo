package com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.syncApi.GenericSyncRepository

class SyncVMFactory (private val repository: GenericSyncRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            return SyncViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}