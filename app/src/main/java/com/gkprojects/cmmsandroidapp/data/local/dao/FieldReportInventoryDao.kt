package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData

@Dao
interface FieldReportInventoryDao {

    @Query("Select * from FieldReportInventory")
    fun getAllFieldReportInventory(): LiveData<List<FieldReportInventory>>
    @Insert
    suspend fun addFieldReportInventory(fieldReportInventory: FieldReportInventory)

    @Update
    suspend fun updateFieldReportInventory(fieldReportInventory: FieldReportInventory)
    @Delete
    suspend fun deleteFieldReportInventory(fieldReportInventory: FieldReportInventory)

    @Query("select Inventory.Description as description, " +
            "Inventory.Title as title, " +
            "FieldReportInventory.FieldReportID as fieldReportID, " +
            "FieldReportInventory.FieldReportInventoryID as fieldReportInventoryID, " +
            "FieldReportInventory.InventoryID as inventoryID " +
            "from FieldReportInventory " +
            "left join Inventory on Inventory.InventoryID =FieldReportInventory.InventoryID " +
            "where FieldReportInventory.FieldReportID = :id ")
    fun getFieldReportInventoryByID(id :String):LiveData<List<FieldReportInventoryCustomData>>

    @Query("SELECT * FROM FieldReportInventory")
    suspend fun getAllFieldReportInventoryList(): List<FieldReportInventory> // <--- For sync

    @Query("SELECT * FROM FieldReportInventory WHERE FieldReportInventoryID = :id LIMIT 1")
    suspend fun getFieldReportInventoryByIDNow(id: String): FieldReportInventory? // <--- For sync

}