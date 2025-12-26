package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Departments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoDepartments {
    companion object {
        var userDatabase: CMMSDatabase? = null

        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }
        suspend fun insert(context: Context, department: Departments) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            department.DateCreated=now
            department.LastModified=now
            userDatabase = initialiseDB(context)
            userDatabase!!.DepartmentsDao().addDepartments(department)
        }

        suspend fun delete(context: Context, department: Departments) {
            userDatabase = initialiseDB(context)
            userDatabase!!.DepartmentsDao().deleteDepartments(department)

        }

        suspend fun update(context: Context, department: Departments) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            department.LastModified=now
            userDatabase = initialiseDB(context)
            userDatabase!!.DepartmentsDao().updateDepartments(department)
        }

        suspend fun getAll(context: Context): LiveData<List<Departments>> {
            userDatabase = initialiseDB(context)
            return userDatabase!!.DepartmentsDao().getAllDepartments()

        }
        suspend fun getAllListForSync(context: Context): List<Departments> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.DepartmentsDao().getAllDepartmentsList()  // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, departments: List<Departments>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                departments.forEach { department ->
                    val existing = userDatabase!!.DepartmentsDao()
                        .getDepartmentsByIDNow(department.DeparmentID)  // This should be suspend
                    if (existing == null) {
                        userDatabase!!.DepartmentsDao().addDepartments(department)
                    } else {
                        userDatabase!!.DepartmentsDao().updateDepartments(department)
                    }
                }
            }
        }


    }
}