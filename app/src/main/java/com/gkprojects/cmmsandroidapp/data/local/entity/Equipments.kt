package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Equipments",
    foreignKeys = [ForeignKey(entity = Customer::class,
        childColumns = ["CustomerID"],
        parentColumns = ["CustomerID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE)]
)
data class Equipments(
    @PrimaryKey var EquipmentID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Name") var Name: String?,
    @ColumnInfo(name = "SerialNumber") var SerialNumber: String?,
    @ColumnInfo(name = "Model") var Model: String?,
    @ColumnInfo(name = "Manufacturer") var Manufacturer: String?,
    @ColumnInfo(name = "Notes") var Notes: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "EquipmentVersion") var EquipmentVersion: String?,
    @ColumnInfo(name = "EquipmentCategory") var EquipmentCategory: String?,
    @ColumnInfo(name = "Warranty") var Warranty: String?,
    @ColumnInfo(name = "EquipmentStatus") var EquipmentStatus: Boolean?,
    @ColumnInfo(name = "InstallationDate") var InstallationDate: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?,
    @ColumnInfo(name = "CustomerID") var CustomerID: String?
)