package com.gkprojects.cmmsandroidapp.presentation.ui.Inventory

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoInventory
import kotlinx.coroutines.launch

class InventoryVM:ViewModel() {
    private var hasCredentials:Boolean =true

    fun insertInventory(context: Context,inventory: Inventory){
        RepoInventory.insertInventory(context, inventory)
    }
    fun updateInventory(context: Context,inventory: Inventory){
        RepoInventory.updateInventory(context, inventory)
    }
    fun deleteInventory(context: Context,inventory: Inventory,onResult: (OperationResult<Unit>) -> Unit ){
        viewModelScope.launch {
            try {
                if (inventory==null){
                    onResult(OperationResult.Error("Inventory not found"))
                    return@launch
                }
                if(hasCredentials){
                    RepoInventory.deleteInventory(context, inventory)
                    onResult(OperationResult.Success())
                }else{
                    onResult(OperationResult.Error("No credentials"))
                }


            }catch (e:SQLiteConstraintException){
                throw e
            }

        }

    }
    fun getAllInventory(context: Context):LiveData<List<Inventory>>{
        return RepoInventory.getInventories(context)
    }
    fun getSingleInventory(context: Context,id:String):LiveData<Inventory>{
        return RepoInventory.getSingleInventory(context,id)
    }
}