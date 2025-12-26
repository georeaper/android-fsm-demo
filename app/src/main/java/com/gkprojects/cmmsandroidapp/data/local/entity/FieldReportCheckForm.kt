package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "FieldReportCheckForm",
    foreignKeys = [ForeignKey(entity = FieldReportEquipment::class,
        childColumns = ["FieldReportEquipmentID"],
        parentColumns = ["FieldReportEquipmentID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
)
data class FieldReportCheckForm(
    @PrimaryKey var FieldReportCheckFormID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "FieldReportEquipmentID") var FieldReportEquipmentID: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "ValueExpected") var ValueExpected: String?,
    @ColumnInfo(name = "ValueMeasured") var ValueMeasured: String?,
    @ColumnInfo(name = "Result") var Result: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)
