package com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.remote.api.ApiRequest
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.syncApi.GenericSyncRepository
import kotlinx.coroutines.launch

class SyncViewModel(private val repository: GenericSyncRepository) : ViewModel() {

    private val _entities = MutableLiveData<List<Any>>()
    val entities: LiveData<List<Any>> get() = _entities

    fun fetchEntityData(entityName: String, timestamp: String, entityType: Class<*>) {
        viewModelScope.launch {
            when (val result = repository.fetchEntities(ApiRequest(entityName,timestamp), entityType)) {
                is GenericSyncRepository.Result.Success -> {
                    _entities.postValue(result.data as List<Any>)
                }
                is GenericSyncRepository.Result.Error -> {
                    // Handle error appropriately, e.g., log, show a toast, update UI with an error state
                    // Example:
                    // Log.e("SyncViewModel", "Error fetching data", result.exception)
                    // _entities.postValue(listOf()) // Or set a specific error state
                    // showToast("An error occurred.") // You can create a function to do that
                }
            }
        }
    }


}