package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderTools
import kotlinx.coroutines.launch

class FieldReportToolsVM(private val repository: RepoWorkOrderTools) : ViewModel() {

    fun insert ( fieldReportTools: FieldReportTools){
        viewModelScope.launch{
            repository.insertWorkOrderTools(fieldReportTools)
        }

    }
    fun delete (fieldReportTools: FieldReportTools){
        viewModelScope.launch{
            repository.deleteWorkOrderTools(fieldReportTools)
        }

    }

    fun getTollsByReportID( id :String) :LiveData<List<FieldReportToolsCustomData>>{
        return  repository.getWorkOrderToolsByReportID( id)
    }

}