package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset

@Dao
interface ModelDao {

    @Query("Select * from ModelAsset")
    suspend fun selectAllModels():List<ModelAsset>
    @Insert
    suspend fun insertModel(model : ModelAsset):Long

    @Update
    suspend fun updateModel(model: ModelAsset):Int

    @Delete
    suspend fun deleteModel(model: ModelAsset):Int
    @Query("SELECT * FROM ModelAsset")
    suspend fun getAllModelAssetList(): List<ModelAsset> // <--- For sync

    @Query("SELECT * FROM ModelAsset WHERE ModelID = :id LIMIT 1")
    suspend fun getModelAssetByIDNow(id: String): ModelAsset? // <--- For sync
}