package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gkprojects.cmmsandroidapp.data.local.entity.Users

class sharedUsersVM : ViewModel(){
    val user =MutableLiveData<Users>()

}