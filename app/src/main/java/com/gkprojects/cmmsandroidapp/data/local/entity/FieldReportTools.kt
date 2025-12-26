package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "FieldReportTools",
    foreignKeys = [
        ForeignKey(entity = FieldReports::class,
            childColumns = ["FieldReportID"],
            parentColumns = ["FieldReportID"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity =Tools::class,
            childColumns = ["ToolsID"],
            parentColumns = ["ToolsID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)
        ])

data class FieldReportTools(
    @PrimaryKey var FieldReportToolsID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "FieldReportID") var FieldReportID:String?,
    @ColumnInfo(name = "ToolsID") var ToolsID: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)