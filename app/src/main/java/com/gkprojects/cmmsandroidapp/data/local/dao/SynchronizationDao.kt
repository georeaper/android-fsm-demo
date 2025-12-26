package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.Synchronization

@Dao
interface SynchronizationDao {
    @Query("SELECT * FROM Synchronization")
    suspend fun getAll(): List<Synchronization>

    @Query("SELECT * FROM Synchronization WHERE Entity = :entity")
    suspend fun getLastSyncDate( entity :String): Synchronization

    @Insert
    suspend fun insert(synchronization: Synchronization)

    @Delete
    suspend fun delete(synchronization: Synchronization)

    @Update
    suspend fun update(synchronization: Synchronization)

    @Query("SELECT * FROM Synchronization")
    suspend fun getAllSynchronizationList(): List<Synchronization> // <--- For sync

    @Query("SELECT * FROM Synchronization WHERE SyncID = :id LIMIT 1")
    suspend fun getSynchronizationByIDNow(id: String): Synchronization? // <--- For sync
}