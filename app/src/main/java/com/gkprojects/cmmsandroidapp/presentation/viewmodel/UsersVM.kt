package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersVM: ViewModel() {
    private lateinit var notificationService: NotificationService
    private val _userDetails = MutableLiveData<Users>()
    val userDetails: LiveData<Users> = _userDetails
    fun insertUser(context: Context, users: Users) {
        notificationService = NotificationService.getInstance(context)
        viewModelScope.launch(Dispatchers.IO) {
            RepoUsers.insertUser(context, users)

            // Optional: delay to ensure order
            withContext(Dispatchers.Main) {
                notificationService.addNotification(
                    Notification(
                        id = 1,
                        timeStamp = users.LastModified.toString(),
                        title = "New Work Order",
                        description = "New user: ${users.Name} has been added",
                        type = "Added",
                        function = "None",
                        seen = false
                    )
                )
            }
        }
    }
    suspend fun deleteUser(context: Context, users: Users){
         RepoUsers.deleteUser(context,users)
    }
    fun getAllUsers(context: Context):LiveData<List<Users>>{
        return RepoUsers.getAllUsers(context)
    }
    fun loadSingleUser(context: Context, id: String) {
        viewModelScope.launch {
            val user = RepoUsers.getSingleUser(context, id)
            _userDetails.postValue(user)
        }
    }
    fun increaseLastReportNumber(context: Context,number: Int,id: String){
        RepoUsers.increaseLastReport(context,number,id)
    }
    fun getFirstUser(context: Context):LiveData<Users>{
        return RepoUsers.getFirstUser(context)
    }
    fun updateUser(context: Context,user : Users) {

        viewModelScope.launch(Dispatchers.IO) {
            RepoUsers.updateUser(context, user)
        }

    }

}