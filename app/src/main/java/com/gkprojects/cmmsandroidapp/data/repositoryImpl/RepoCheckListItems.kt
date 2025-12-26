package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportCheckFormsDao
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoCheckListItems private constructor(context: Context) {
    private val dao : FieldReportCheckFormsDao = CMMSDatabase.getInstance(context)!!.FieldReportCheckFormsDao()
        companion object {
            @Volatile
            private var instance: RepoCheckListItems? = null

            fun getInstance(context: Context): RepoCheckListItems {
                return instance ?: synchronized(this) {
                    instance ?: RepoCheckListItems(context).also { instance = it }
                }
            }

        }



        suspend fun insertCheckFormField(fieldReportCheckForm: FieldReportCheckForm){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            fieldReportCheckForm.DateCreated = now
            fieldReportCheckForm.LastModified= now


            dao.insertFieldReportCheckForms(fieldReportCheckForm)

        }
        fun deleteCheckFormField( fieldReportCheckForm: FieldReportCheckForm){

            dao.deleteFieldReportCheckForms(fieldReportCheckForm)

        }
        fun updateCheckFormField( fieldReportCheckForm: FieldReportCheckForm){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            fieldReportCheckForm.LastModified= now

            dao.updateFieldReportCheckForms(fieldReportCheckForm)

        }
        fun getCheckFormFields ( id :String): LiveData<List<FieldReportCheckForm>> {


            return dao.selectFieldReportCheckFormsByFieldReportEquipmentID(id)
        }
        suspend fun getAllListForSync(): List<FieldReportCheckForm> {

            return withContext(Dispatchers.IO) {
                dao.getAllFieldReportCheckFormList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate( list: List<FieldReportCheckForm>)  {

            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = dao.getFieldReportCheckFormByIDNow(it.FieldReportCheckFormID)// This should be suspend
                    if (existing == null) {
                        dao.insertFieldReportCheckForms(it)
                    } else {
                        dao.updateFieldReportCheckForms(it)
                    }
                }
            }
        }
}
