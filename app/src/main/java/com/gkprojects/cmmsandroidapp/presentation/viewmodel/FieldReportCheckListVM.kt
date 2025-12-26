package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckListItems
import kotlinx.coroutines.launch

class FieldReportCheckListVM(private val repository: RepoCheckListItems) :ViewModel() {

    fun getFieldReportCheckListByFieldEquipmentID( id : String):LiveData<List<FieldReportCheckForm>>{
        return repository.getCheckFormFields( id)
    }
    fun insertFieldReportCheckList(fieldReportCheckForm: FieldReportCheckForm){
        viewModelScope.launch {
            repository.insertCheckFormField(fieldReportCheckForm)
        }
    }


}