package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset

import com.gkprojects.cmmsandroidapp.data.repositoryImpl.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    // LiveData for insert success status
    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    // LiveData for delete success status
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess

    // LiveData for loaded categories
    private val _categoryData = MutableLiveData<List<CategoryAsset>>()
    val categoryData: LiveData<List<CategoryAsset>> get() = _categoryData

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Insert a category into the database
    fun insertCategory(categoryAsset: CategoryAsset) {
        viewModelScope.launch {
            try {
                val result = repository.insertCategory(categoryAsset)
                if (result > 0) {
                    _insertSuccess.value = true
                    loadCategories() // Reload categories after insertion
                } else {
                    _insertSuccess.value = false
                }
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }
    }

    // Delete a category from the database
    fun deleteCategories(categoryAsset: CategoryAsset) {
        viewModelScope.launch {
            try {
                val deletion = repository.deleteCategories(categoryAsset)
                if (deletion > 0) {
                    _deleteSuccess.value = true
                    loadCategories() // Reload categories after deletion
                } else {
                    _deleteSuccess.value = false
                }
            } catch (e: Exception) {
                _deleteSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }
    }

    // Load all categories from the database
    fun loadCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getAllCategories()
                _categoryData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }
    }
}
