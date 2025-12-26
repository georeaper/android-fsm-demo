package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ModelRepository


class ModelViewModelFactory(    private val repository: ModelRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModelViewModel::class.java)) {
            return ModelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}