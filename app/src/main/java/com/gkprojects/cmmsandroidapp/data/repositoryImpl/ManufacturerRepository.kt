package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.ManufacturerDao
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ManufacturerRepository private constructor(context: Context) {
    private val manufacturerDao : ManufacturerDao = CMMSDatabase.getInstance(context)!!.ManufacturerDao()

    companion object {
        @Volatile
        private var instance: ManufacturerRepository? = null

        fun getInstance(context: Context): ManufacturerRepository {
            return instance ?: synchronized(this) {
                instance ?: ManufacturerRepository(context).also { instance = it }
            }
        }
    }
    suspend fun insertManufacturer(manufacturer: Manufacturer): Long {
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        manufacturer.DateCreated = now
        manufacturer.LastModified=now
        return manufacturerDao.insertManufacturer(manufacturer)
    }
    suspend fun getAllManufacturer(): List<Manufacturer> {
        return manufacturerDao.selectAllManufacturer()
    }
    suspend fun deleteManufacturer(manufacturer: Manufacturer):Int{
        return manufacturerDao.deleteManufacturer(manufacturer)
    }
    suspend fun getAllListForSync(): List<Manufacturer> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            manufacturerDao.getAllManufacturerList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate( manufacturerList: List<Manufacturer>)  {

        withContext(Dispatchers.IO) {
            manufacturerList.forEach { manufacturer ->
                val existing = manufacturerDao.getManufacturerByIDNow(manufacturer.ManufacturerID)  // This should be suspend
                if (existing == null) {
                    manufacturerDao.insertManufacturer(manufacturer)
                } else {
                    manufacturerDao.updateManufacturer(manufacturer)
                }
            }
        }
    }
}