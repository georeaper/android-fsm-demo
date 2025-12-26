package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Tools")
data class Tools(
    @PrimaryKey var ToolsID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Title") var Title: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "Model") var Model: String?,
    @ColumnInfo(name = "Manufacturer") var Manufacturer: String?,
    @ColumnInfo(name = "SerialNumber") var SerialNumber: String?,
    @ColumnInfo(name = "CalibrationDate") var CalibrationDate: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)
