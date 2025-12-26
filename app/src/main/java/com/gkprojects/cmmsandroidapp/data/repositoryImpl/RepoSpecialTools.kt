package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RepoSpecialTools {
    companion object {
        var userDatabase: CMMSDatabase? = null

        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }

        suspend fun getSingleToolForDeletion(context: Context,id:String):Tools?{
            userDatabase = initialiseDB(context)
            return userDatabase!!.ToolsDao().getToolsByIDNow(id)
        }
        fun insert(context: Context,tools: Tools){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            tools.DateCreated = now
            tools.LastModified= now
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.ToolsDao().insertTools(tools)
            }
        }
        fun getAllTools(context: Context):LiveData<List<Tools>>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.ToolsDao().getAllTools()
        }

        fun getSingleTool(context: Context,id:String):LiveData<Tools>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.ToolsDao().getSingleTool(id)
        }
        fun updateTools(context: Context,tools: Tools){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            tools.LastModified= now
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.ToolsDao().updateTools(tools)
            }
        }
        fun deleteTools(context: Context,tools: Tools){
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.ToolsDao().deleteTools(tools)
            }
        }

        suspend fun getAllListForSync(context: Context): List<Tools> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.ToolsDao().getAllToolsList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, list: List<Tools>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = userDatabase!!.ToolsDao().getToolsByIDNow(it.ToolsID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.ToolsDao().insertTools(it)
                    } else {
                        userDatabase!!.ToolsDao().updateTools(it)
                    }
                }
            }
        }
    }
}