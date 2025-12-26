package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoTasks {
    companion object {
        var userDatabase: CMMSDatabase? = null
        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }

        suspend fun insert(context: Context, task: Tasks) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            task.DateCreated = now
            task.LastModified = now

            userDatabase = initialiseDB(context)
            userDatabase!!.TasksDao().insert(task)
        }

        suspend fun delete(context: Context, task: Tasks) {
            userDatabase = initialiseDB(context)
            userDatabase!!.TasksDao().delete(task)

        }

        suspend fun update(context: Context, task: Tasks) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            task.LastModified = now

            userDatabase = initialiseDB(context)
            userDatabase!!.TasksDao().update(task)
        }

        fun getAllTasks(context: Context): LiveData<List<Tasks>> {
            userDatabase = initialiseDB(context)
            return userDatabase!!.TasksDao().getAll()
        }

        suspend fun getAllListForSync(context: Context): List<Tasks> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.TasksDao()
                    .getAllTasksList() // This should be a suspend DAO function that returns List<Tasks>
            }
        }

        suspend fun insertOrUpdate(context: Context, tasks: List<Tasks>) {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                tasks.forEach { tasks ->
                    val existing = userDatabase!!.TasksDao()
                        .getTasksByIDNow(tasks.TaskID)  // This should be suspend
                    if (existing == null) {
                        userDatabase!!.TasksDao().insert(tasks)
                    } else {
                        userDatabase!!.TasksDao().update(tasks)
                    }
                }
            }
        }
    }
}