package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoInventory {
    companion object {
        var userDatabase: CMMSDatabase? = null

        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }

        fun getInventories(context: Context):LiveData<List<Inventory>>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.InventoryDao().getAllInventory()
        }
        fun getSingleInventory(context: Context,id:String):LiveData<Inventory>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.InventoryDao().getSingleInventory(id)
        }
        fun insertInventory(context: Context,inventory: Inventory){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            inventory.DateCreated = now
            inventory.LastModified= now
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.InventoryDao().addInventory(inventory)
            }
        }
        fun updateInventory(context: Context,inventory: Inventory){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            inventory.LastModified= now
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.InventoryDao().updateInventory(inventory)
            }
        }
        suspend fun deleteInventory(context: Context,inventory: Inventory){
            userDatabase = initialiseDB(context)
            userDatabase!!.InventoryDao().deleteInventory(inventory)

        }
        suspend fun getAllListForSync(context: Context): List<Inventory> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.InventoryDao().getAllInventoryList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, list: List<Inventory>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = userDatabase!!.InventoryDao().getInventoryByIDNow(it.InventoryID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.InventoryDao().addInventory(it)
                    } else {
                        userDatabase!!.InventoryDao().updateInventory(it)
                    }
                }
            }
        }
    }

}