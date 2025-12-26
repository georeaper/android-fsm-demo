package com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncData

class SyncDataViewModelFactory(private val syncDataApi: SyncData) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncDataViewModel::class.java)) {
            return SyncDataViewModel(syncDataApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}