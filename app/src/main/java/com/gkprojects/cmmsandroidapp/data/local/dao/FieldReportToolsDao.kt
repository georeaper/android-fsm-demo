package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData

@Dao
interface FieldReportToolsDao {
    @Query("Select " +
            "Tools.Title as toolsTitle , " +
            " Tools.SerialNumber as toolsSerialNumber , " +
            "Tools.CalibrationDate as toolsCalDate, " +
            "FieldReportTools.ToolsID as toolsID, " +
            "FieldReportTools.FieldReportID as fieldReportID, " +
            "FieldReportTools.FieldReportToolsID as fieldReportToolsID " +
            "from FieldReportTools " +
            "left join Tools on Tools.ToolsID =FieldReportTools.ToolsID " +
            "where  FieldReportTools.FieldReportID= :id ")
    fun getFieldReportToolsByID(id :String) :LiveData<List<FieldReportToolsCustomData>>

    @Insert
    suspend fun insertFieldReportTool(fieldReportTools: FieldReportTools)

    @Delete
    suspend fun deleteFieldReportTool(fieldReportTools: FieldReportTools)

    @Update
    suspend fun updateFieldReportTools(fieldReportTools: FieldReportTools)

    @Query("SELECT * FROM FieldReportTools")
    suspend fun getAllFieldReportToolsList(): List<FieldReportTools> // <--- For sync

    @Query("SELECT * FROM FieldReportTools WHERE FieldReportToolsID = :id LIMIT 1")
    suspend fun getFieldReportToolsByIDNow(id: String): FieldReportTools? // <--- For sync
}