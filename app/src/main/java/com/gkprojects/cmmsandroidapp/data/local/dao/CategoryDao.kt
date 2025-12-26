package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset

@Dao
interface CategoryDao {
    @Query("Select * from CategoryAsset")
    suspend fun selectAllCategories():List<CategoryAsset>

    @Insert
    suspend fun insertCategories(category : CategoryAsset):Long

    @Update
    suspend fun updateCategories(category: CategoryAsset):Int

    @Delete
    suspend fun deleteCategories(category: CategoryAsset):Int

    @Query("SELECT * FROM CategoryAsset")
    suspend fun getAllCategoryAssetList(): List<CategoryAsset> // <--- For sync

    @Query("SELECT * FROM CategoryAsset WHERE CategoryAssetID = :id LIMIT 1")
    suspend fun getCategoryAssetByIDNow(id: String): CategoryAsset? // <--- For sync

}