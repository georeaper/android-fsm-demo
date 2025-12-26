package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.ModelDao
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ModelRepository private constructor(context: Context) {
    private val modelDao : ModelDao = CMMSDatabase.getInstance(context)!!.ModelDao()

    companion object {
        @Volatile
        private var instance: ModelRepository? = null

        fun getInstance(context: Context): ModelRepository {
            return instance ?: synchronized(this) {
                instance ?: ModelRepository(context).also { instance = it }
            }
        }
    }
    suspend fun insertModel(model : ModelAsset): Long {
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        model.DateCreated = now
        model.LastModified=now
        return modelDao.insertModel(model)
    }
    suspend fun getAllModels(): List<ModelAsset> {
        return modelDao.selectAllModels()
    }
    suspend fun deleteModels(model : ModelAsset):Int{
        return modelDao.deleteModel(model)
    }
    suspend fun getAllListForSync(): List<ModelAsset> {

        return withContext(Dispatchers.IO) {
            modelDao.getAllModelAssetList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate( modelList: List<ModelAsset>)  {

        withContext(Dispatchers.IO) {
            modelList.forEach { model ->
                val existing = modelDao.getModelAssetByIDNow(model.ModelID)  // This should be suspend
                if (existing == null) {
                    modelDao.insertModel(model)
                } else {
                    modelDao.updateModel(model)
                }
            }
        }
    }
}