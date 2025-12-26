package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gkprojects.cmmsandroidapp.data.local.entity.Users

class SharedViewModel: ViewModel() {
    val reportId = MutableLiveData<String>()
    val customerId =MutableLiveData<String>()
    val user =MutableLiveData<Users>()
}