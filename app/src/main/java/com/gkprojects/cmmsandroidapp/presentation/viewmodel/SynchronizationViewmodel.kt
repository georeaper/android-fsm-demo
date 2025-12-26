package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.Synchronization
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SynchronizationRepository
import kotlinx.coroutines.launch

class SynchronizationViewmodel(private val repository: SynchronizationRepository) : ViewModel(){

    private val _synchronization = MutableLiveData<Synchronization>()
    val synchronization: LiveData<Synchronization> get() = _synchronization

    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getLastSynchronization(date :String){
        viewModelScope.launch{
            try {
                val result = repository.getLastSyncDate(date)
                _synchronization.value = result

            }catch (e :Exception){
                _insertSuccess.value = false
                _error.value = e.message
            }
        }


    }


}