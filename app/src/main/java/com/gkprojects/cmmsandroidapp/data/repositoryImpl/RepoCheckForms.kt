package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.CheckForms
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoCheckForms {
    companion object {
        var userDatabase: CMMSDatabase? = null

        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }
         fun insertCheckFormField(context:Context,checkForms: CheckForms){
             val currentDateTime = Calendar.getInstance().time
             val now = DateUtils.format(currentDateTime)
             checkForms.DateCreated =now
             checkForms.LastModified=now

            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {

                userDatabase!!.CheckFormsDao().addCheckFormsFields(checkForms)
            }
        }
         fun deleteCheckFormField(context:Context,checkForms: CheckForms){
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                //userDatabase!!.hospitalDAO().addHospital(hospital)
                userDatabase!!.CheckFormsDao().deleteCheckFormsFields(checkForms)
            }
        }
         fun getCheckFormFields (context: Context,id :String): LiveData<List<CheckForms>> {
            userDatabase = initialiseDB(context)
            return userDatabase!!.CheckFormsDao().getCheckFormsFieldsByMaintenanceID(id)
        }
        suspend fun getAllListForSync(context: Context): List<CheckForms> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.CheckFormsDao().getAllCheckFormsList()  // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, checkForms: List<CheckForms>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                checkForms.forEach {
                    val existing = userDatabase!!.CheckFormsDao().getCheckFormsByIDNow(it.CheckFormID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.CheckFormsDao().addCheckFormsFields(it)
                    } else {
                        userDatabase!!.CheckFormsDao().updateCheckFormsFields(it)
                    }
                }
            }
        }
    }
}