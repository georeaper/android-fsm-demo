package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoUsers {

    companion object{
        var userDatabase: CMMSDatabase?=null

        private fun initialiseDB(context: Context): CMMSDatabase?
        {
            return CMMSDatabase.getInstance(context)!!
        }

        fun insertUser(context: Context, users: Users)
        {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            users.DateCreated = now
            users.LastModified= now
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                //userDatabase!!.hospitalDAO().addHospital(hospital)
                userDatabase!!.UsersDao().addUsers(users)
            }
        }
        fun deleteUser(context: Context,users: Users){
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                //userDatabase!!.hospitalDAO().addHospital(hospital)
                userDatabase!!.UsersDao().deleteUsers(users)
            }
        }

        fun getAllUsers(context: Context): LiveData<List<Users>>
        {
            userDatabase = initialiseDB(context)
            //return userDatabase!!.hospitalDAO().getAllHospitals()
            return userDatabase!!.UsersDao().getAllUsers()
        }
        fun updateUser(context: Context, users: Users){
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

            users.LastModified=dateFormat.format(currentDateTime)
            userDatabase = initialiseDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                //userDatabase!!.hospitalDAO().addHospital(hospital)
                userDatabase!!.UsersDao().updateUsers(users)
            }
        }
        suspend fun getSingleUser(context:Context, id :String): Users {
            userDatabase = initialiseDB(context)
            return userDatabase!!.UsersDao().getUserByID(id)
        }
        fun increaseLastReport(context: Context,number:Int ,id :String){
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                userDatabase!!.UsersDao().increaseLastReportNumber(number,id)
            }

        }
        fun getFirstUser(context: Context):LiveData<Users>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.UsersDao().getFirstUser()
        }

        suspend fun getAllListForSync(context: Context): List<Users> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.UsersDao().getAllUsersList() // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, list: List<Users>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                list.forEach {
                    val existing = userDatabase!!.UsersDao().getUsersByIDNow(it.UserID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.UsersDao().addUsers(it)
                    } else {
                        userDatabase!!.UsersDao().updateUsers(it)
                    }
                }
            }
        }


    }
}