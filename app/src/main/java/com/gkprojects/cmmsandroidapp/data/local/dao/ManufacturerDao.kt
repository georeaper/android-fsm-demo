package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer

@Dao
interface ManufacturerDao {

    @Query("Select * from Manufacturer")
    suspend fun selectAllManufacturer():List<Manufacturer>

    @Insert
    suspend fun insertManufacturer(manufacturer : Manufacturer):Long

    @Update
    suspend fun updateManufacturer(manufacturer: Manufacturer):Int

    @Delete
    suspend fun deleteManufacturer(manufacturer: Manufacturer) :Int

    @Query("SELECT * FROM Manufacturer")
    suspend fun getAllManufacturerList(): List<Manufacturer> // <--- For sync

    @Query("SELECT * FROM Manufacturer WHERE ManufacturerID = :id LIMIT 1")
    suspend fun getManufacturerByIDNow(id: String): Manufacturer? // <--- For sync
}