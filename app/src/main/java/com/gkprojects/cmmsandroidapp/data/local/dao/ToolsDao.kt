package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools

@Dao
interface ToolsDao{

    @Query("Select * from Tools")
    fun getAllTools(): LiveData<List<Tools>>

    @Query("Select * from Tools Where ToolsID= :id ")
    fun getSingleTool(id :String):LiveData<Tools>

    @Update
    fun updateTools(tool : Tools)

    @Insert
    fun insertTools(tool : Tools)

    @Delete
    fun deleteTools(tools: Tools)

    @Query("SELECT * FROM Tools")
    suspend fun getAllToolsList(): List<Tools> // <--- For sync

    @Query("SELECT * FROM Tools WHERE ToolsID = :id LIMIT 1")
    suspend fun getToolsByIDNow(id: String): Tools? // <--- For sync

}