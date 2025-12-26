package com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoSpecialTools
import kotlinx.coroutines.launch

class SpecialToolsVM : ViewModel() {
    private val hasCredentials :Boolean = true

    fun insertTools(context: Context,tools: Tools){

        RepoSpecialTools.insert(context,tools)
    }
    fun updateTools(context: Context,tools: Tools){
        RepoSpecialTools.updateTools(context,tools)
    }
    fun deleteTools(context: Context,tools: Tools){
        RepoSpecialTools.deleteTools(context, tools)
    }
    fun delete(context: Context, id: String,onResult: (OperationResult<Unit>) -> Unit){
        viewModelScope.launch {
            try {
                val tool=RepoSpecialTools.getSingleToolForDeletion(context, id)
                if (tool==null){
                    onResult(OperationResult.Error("Tool not found"))
                    return@launch
                }
                if(hasCredentials){
                    RepoSpecialTools.deleteTools(context, tool)
                    onResult(OperationResult.Success())


                }
            }catch (e:SQLiteConstraintException){
                throw e
            }

        }


    }

    fun getTools(context: Context):LiveData<List<Tools>>{
        return RepoSpecialTools.getAllTools(context)
    }
    fun getSingleTool(context: Context,id:String):LiveData<Tools>{
        return RepoSpecialTools.getSingleTool(context, id)
    }
}