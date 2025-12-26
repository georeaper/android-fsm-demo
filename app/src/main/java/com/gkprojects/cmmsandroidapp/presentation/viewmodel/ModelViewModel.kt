package com.gkprojects.cmmsandroidapp.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ModelRepository

import kotlinx.coroutines.launch

class ModelViewModel (private val repository: ModelRepository) : ViewModel() {

    // LiveData for insert success status
    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess
    // LiveData for loaded settings
    private val _modelAssetData = MutableLiveData<List<ModelAsset>>()
    val modelAssetData: LiveData<List<ModelAsset>> get() = _modelAssetData

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Function to insert settings
    fun insertModelAsset(model: ModelAsset) {
        viewModelScope.launch {
            try {
                val result = repository.insertModel(model)
                _insertSuccess.value = result > 0
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }
    }

    // Function to load settings by key
    fun loadAllModelAsset() {
        viewModelScope.launch {
            try {
                val result = repository.getAllModels()
                _modelAssetData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }
    }
    fun deleteModelAsset(model: ModelAsset){
        viewModelScope.launch {
            try {
                val deletion = repository.deleteModels(model)
                _deleteSuccess.value= deletion>0
            }catch (e:Exception){
                _deleteSuccess.value=false
                _error.value=e.message

            }
        }
    }
}