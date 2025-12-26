package com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.EquipmentVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment

//class EquipmentViewModelFactory (private val repo: RepoEquipment) : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(EquipmentVM::class.java)) {
//            return EquipmentVM(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

class EquipmentViewModelFactory(private val repo: RepoEquipment) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.e("VMFactory", "Requested: ${modelClass.name}, Expected: ${EquipmentVM::class.java.name}")

        if (modelClass == EquipmentVM::class.java) {
            return EquipmentVM(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}