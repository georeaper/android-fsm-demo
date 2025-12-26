package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.ContractsDao
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractsCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.entity.Contracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class RepoContracts private constructor(context: Context){
    private val contractsDao : ContractsDao = CMMSDatabase.getInstance(context)!!.ContractsDao()

    companion object{
        @Volatile
        private var instance: RepoContracts? = null

        fun getInstance(context: Context): RepoContracts {
            return instance ?: synchronized(this) {
                instance ?: RepoContracts(context).also { instance = it }
            }
        }
    }

        suspend fun insert(contracts: Contracts) :Long
        {
            contracts.ContractID=UUID.randomUUID().toString()
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            contracts.DateCreated = now
            contracts.LastModified= now
            return contractsDao.addContracts(contracts)

        }

        suspend fun delete( contracts: Contracts):Int{

            return contractsDao.deleteContracts(contracts)

        }

       suspend fun getAllContractData(): List<Contracts>
        {
            return contractsDao.getAllContracts()
        }

       suspend fun updateContractData( contract: Contracts) :Int{

           val currentDateTime = Calendar.getInstance().time
           val now = DateUtils.format(currentDateTime)

            contract.LastModified = now
           return contractsDao.updateContracts(contract)

        }
       suspend fun getCustomerIdData(): List<CustomerSelect> {

            return contractsDao.getCustomerID()

        }


        suspend fun getListContracts(): List<ContractsCustomerName>{

            return contractsDao.getContractsCustomerNames()

        }
       suspend fun getContractsById(id :String): Contracts {

            return contractsDao.getContractsById(id)
        }


    suspend fun getAllListForSync(): List<Contracts> {
        //userDatabase = initialiseDB(context)
        return withContext(Dispatchers.IO) {
            contractsDao.getAllContractsList()  // This should be a suspend DAO function that returns <List<Departments>>
        }
    }

    suspend fun insertOrUpdate(lists: List<Contracts>)  {

        withContext(Dispatchers.IO) {
            lists.forEach {
                val existing = contractsDao.getContractsByIDNow(it.ContractID)  // This should be suspend
                if (existing == null) {
                    contractsDao.addContracts(it)
                } else {
                    contractsDao.updateContracts(it)
                }
            }
        }
    }




}