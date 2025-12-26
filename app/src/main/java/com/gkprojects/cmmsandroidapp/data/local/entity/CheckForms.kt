package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "CheckForms")
data class CheckForms(
    @PrimaryKey var CheckFormID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "MaintenancesID") var MaintenancesID: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "ValueExpected") var ValueExpected: String?,
    @ColumnInfo(name = "ValueType") var ValueType: String?, //checkbox, Textview, Edittext, etc
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)
