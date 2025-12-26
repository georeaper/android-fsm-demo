package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ManufacturerRepository
import kotlinx.coroutines.launch

class ManufacturerViewModel (private val repository: ManufacturerRepository) : ViewModel() {

    // LiveData for insert success status
    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess
    // LiveData for loaded settings
    private val _manufacturerData = MutableLiveData<List<Manufacturer>>()
    val manufacturerData: LiveData<List<Manufacturer>> get() = _manufacturerData

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Function to insert settings
    fun insertManufacturer(manufacturer: Manufacturer) {
        viewModelScope.launch {
            try {
                val result = repository.insertManufacturer(manufacturer)
                _insertSuccess.value = result > 0
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }
    }

    // Function to load settings by key
    fun loadManufacturer() {
        viewModelScope.launch {
            try {
                val result = repository.getAllManufacturer()
                _manufacturerData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }
    }
    fun deleteManufacturer(manufacturer: Manufacturer){
        viewModelScope.launch {
            try {
                val deletion = repository.deleteManufacturer(manufacturer)
                _deleteSuccess.value= deletion>0
            }catch (e:Exception){
                _deleteSuccess.value=false
                _error.value=e.message

            }
        }
    }
}