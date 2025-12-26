package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "FieldReportEquipment",
    foreignKeys = [
        ForeignKey(entity = FieldReports::class,
            childColumns = ["FieldReportID"],
            parentColumns = ["FieldReportID"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Equipments::class,
            childColumns = ["EquipmentID"],
            parentColumns = ["EquipmentID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Maintenances::class,
            childColumns = ["MaintenanceID"],
            parentColumns = ["MaintenanceID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)]
)
data class FieldReportEquipment(
    @PrimaryKey var FieldReportEquipmentID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "CompletedStatus") var CompletedStatus: Boolean?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?,
    @ColumnInfo(name = "FieldReportID") var FieldReportID: String?,
    @ColumnInfo(name = "EquipmentID") var EquipmentID: String?,
    @ColumnInfo(name = "MaintenanceID") var MaintenanceID: String?
)