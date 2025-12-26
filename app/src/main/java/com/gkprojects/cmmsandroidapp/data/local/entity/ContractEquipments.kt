package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ContractEquipments",
    foreignKeys = [
        ForeignKey(entity = Equipments::class,
            childColumns = ["EquipmentID"],
            parentColumns = ["EquipmentID"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Contracts::class,
            childColumns = ["ContractID"],
            parentColumns = ["ContractID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)]
)
data class ContractEquipments(
    @PrimaryKey var ContractEquipmentID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Value") var Value: Double?,
    @ColumnInfo(name = "Visits") var Visits: Double?,
    @ColumnInfo(name = "ContractID") var ContractID: String?,
    @ColumnInfo(name = "EquipmentID") var EquipmentID: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)
