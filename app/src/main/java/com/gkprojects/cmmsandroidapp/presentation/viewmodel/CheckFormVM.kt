package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gkprojects.cmmsandroidapp.data.local.entity.CheckForms
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckForms

class CheckFormVM: ViewModel() {

     fun insert(context: Context, checkForms: CheckForms)
    {

        RepoCheckForms.insertCheckFormField(context,checkForms)
    }
    fun delete(context: Context,checkForms: CheckForms){
        RepoCheckForms.deleteCheckFormField(context,checkForms)
    }
    fun getCheckFormFields(context: Context,id :String):LiveData<List<CheckForms>>{
        return RepoCheckForms.getCheckFormFields(context,id)
    }

}