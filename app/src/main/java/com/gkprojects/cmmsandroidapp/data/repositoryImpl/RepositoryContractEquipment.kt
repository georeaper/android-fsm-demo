package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils

import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.ContractEquipmentsDao

import com.gkprojects.cmmsandroidapp.data.local.entity.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.local.dto.DetailedContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class RepositoryContractEquipment private constructor(context: Context){
    private val contractEquipmentsDao : ContractEquipmentsDao = CMMSDatabase.getInstance(context)!!.ContractEquipmentsDao()
    companion object{
        @Volatile
        private var instance: RepositoryContractEquipment? = null

        fun getInstance(context: Context): RepositoryContractEquipment {
            return instance ?: synchronized(this) {
                instance ?: RepositoryContractEquipment(context).also { instance = it }
            }
        }
    }
    suspend fun getContractEquipmentsById(id : String): List<ContractEquipments> {
        return contractEquipmentsDao.getContractEquipmentByID(id)

    }
    suspend  fun getDetailedContractByID( id : String): List<DetailedContract> {

        return contractEquipmentsDao.getDetailedContractByID(id)

    }
    suspend  fun deleteContractEquipment(contractEquipment : ContractEquipments) :Int{

       return contractEquipmentsDao.deleteContractEquipments(contractEquipment)


    }

   suspend fun getContractEquipmentByContractEquipmentID( id: String): ContractEquipments {
        return contractEquipmentsDao.getContractEquipmentByEquipmentID(id)

    }

    suspend fun insertContractEquipmentIfNotExists( contractEquipment: ContractEquipments) :Long {
        contractEquipment.ContractEquipmentID=UUID.randomUUID().toString()
        val currentDateTime = Calendar.getInstance().time
        val now = DateUtils.format(currentDateTime)
        contractEquipment.DateCreated = now
        contractEquipment.LastModified= now
        return contractEquipmentsDao.addContractEquipments(contractEquipment)
    }

    suspend fun getAllListForSync(): List<ContractEquipments> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            contractEquipmentsDao.getAllContractEquipmentsList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate(lists: List<ContractEquipments>)  {

        withContext(Dispatchers.IO) {
            lists.forEach {
                val existing = contractEquipmentsDao.getContractEquipmentByIDNow(it.ContractEquipmentID)  // This should be suspend
                if (existing == null) {
                    contractEquipmentsDao.addContractEquipments(it)
                } else {
                    contractEquipmentsDao.updateContractEquipments(it)
                }
            }
        }
    }

}