package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase

import com.gkprojects.cmmsandroidapp.data.local.dao.EquipmentsDao
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentListInCases
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoEquipment private constructor(context: Context) {
    private val equipmentDao : EquipmentsDao = CMMSDatabase.getInstance(context)!!.EquipmentsDAO()

    companion object {
        @Volatile
        private var instance: RepoEquipment? = null

        fun getInstance(context: Context): RepoEquipment {
            return instance ?: synchronized(this) {
                instance ?: RepoEquipment(context).also { instance = it }
            }
        }
    }
        suspend fun getEquipmentsDataByCustomerID(id: String):List<Equipments>{

            return equipmentDao.getAllDataEquipmentsByCustomerID(id)
        }


        suspend fun insert(equipment: Equipments)
        {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            equipment.DateCreated = now
            equipment.LastModified= now
            equipmentDao.addEquipments(equipment)

        }


        fun getAllEquipmentData(): LiveData<List<Equipments>>
        {

            return equipmentDao.getAllEquipments()
        }
        suspend fun updateEquipmentData(equipments: Equipments){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            equipments.LastModified=now
            if (equipments.Model==""){
                equipments.Model=null
            }
            if (equipments.EquipmentCategory==""){
                equipments.EquipmentCategory=null
            }
            if (equipments.Manufacturer==""){
                equipments.Manufacturer=null
                }
            equipmentDao.updateEquipments(equipments)
        }
        suspend fun getCustomerID():List<CustomerSelect>{

            return equipmentDao.getCustomerID()
        }
        suspend fun getCustomerNameDashboard():List<EquipmentSelectCustomerName>{

            return equipmentDao.getCustomerName()
        }

        suspend fun delete(equipments: Equipments){
            equipmentDao.delete(equipments)
        }
        suspend fun getRecordById(id :String): Equipments {

            return equipmentDao.SelectRecordById(id)
        }
        suspend fun getEquipmentByCustomer (id : String):List<EquipmentListInCases>{

            return equipmentDao.selectEquipmentByCustomerID(id)
        }
        suspend fun getTicketsByEquipmentId(id: String):List<Tickets>{

            return equipmentDao.getTicketsByEquipmentId(id)
        }

        suspend fun getAllListForSync(): List<Equipments> {

            return equipmentDao.getAllEquipmentsList()

        }

        suspend fun insertOrUpdate( list: List<Equipments>)  {

            withContext(IO) {
                list.forEach {
                    val existing = equipmentDao.getEquipmentsByIDNow(it.EquipmentID)// This should be suspend
                    if (existing == null) {
                        equipmentDao.addEquipments(it)
                    } else {
                        equipmentDao.updateEquipments(it)
                    }
                }
            }
        }
    }




