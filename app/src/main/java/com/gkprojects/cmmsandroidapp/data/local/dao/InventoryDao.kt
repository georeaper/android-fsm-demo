package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory

@Dao
interface InventoryDao {

    @Query("Select * from Inventory")
    fun getAllInventory(): LiveData<List<Inventory>>

    @Query("Select * from Inventory where InventoryID=:id")
    fun getSingleInventory(id : String ):LiveData<Inventory>
    @Insert
    fun addInventory(inventory: Inventory)

    @Update
    fun updateInventory(inventory: Inventory)
    @Delete
    suspend fun deleteInventory(inventory: Inventory)

    @Query("SELECT * FROM Inventory")
    suspend fun getAllInventoryList(): List<Inventory> // <--- For sync

    @Query("SELECT * FROM Inventory WHERE InventoryID = :id LIMIT 1")
    suspend fun getInventoryByIDNow(id: String): Inventory? // <--- For sync


}