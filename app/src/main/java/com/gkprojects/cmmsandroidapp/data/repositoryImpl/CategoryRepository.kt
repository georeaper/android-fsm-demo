package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.CategoryDao
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CategoryRepository private constructor(context: Context) {
    private val categoryDao : CategoryDao = CMMSDatabase.getInstance(context)!!.CategoryDao()

    companion object {
        @Volatile
        private var instance: CategoryRepository? = null

        fun getInstance(context: Context): CategoryRepository {
            return instance ?: synchronized(this) {
                instance ?: CategoryRepository(context).also { instance = it }
            }
        }
    }
    suspend fun insertCategory(category : CategoryAsset): Long {
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        category.DateCreated = now
        category.LastModified=now
        return categoryDao.insertCategories(category)
    }
    suspend fun getAllCategories(): List<CategoryAsset> {
        return categoryDao.selectAllCategories()
    }
    suspend fun deleteCategories(category : CategoryAsset):Int{
        return categoryDao.deleteCategories(category)
    }
    suspend fun getAllListForSync(): List<CategoryAsset> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            categoryDao.getAllCategoryAssetList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate(categoryLists: List<CategoryAsset>)  {

        withContext(Dispatchers.IO) {
            categoryLists.forEach { category ->
                val existing = categoryDao.getCategoryAssetByIDNow(category.CategoryAssetID)  // This should be suspend
                if (existing == null) {
                    categoryDao.insertCategories(category)
                } else {
                    categoryDao.updateCategories(category)
                }
            }
        }
    }
}