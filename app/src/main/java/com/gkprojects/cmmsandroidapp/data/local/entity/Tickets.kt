package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Tickets",
    foreignKeys = [
        ForeignKey(entity = Customer::class,
            childColumns = ["CustomerID"],
            parentColumns = ["CustomerID"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Users::class,
            childColumns = ["UserID"],
            parentColumns = ["UserID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Equipments::class,
            childColumns = ["EquipmentID"],
            parentColumns = ["EquipmentID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)
    ])
data class Tickets(
    @PrimaryKey var TicketID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Title") var Title: String?,
    @ColumnInfo(name = "TicketNumber") var TicketNumber: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "Notes") var Notes: String?,
    @ColumnInfo(name = "Urgency") var Urgency: String?,
    @ColumnInfo(name = "Active") var Active: Boolean?,
    @ColumnInfo(name = "DateStart") var DateStart: String?,
    @ColumnInfo(name = "DateEnd") var DateEnd: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?,
    @ColumnInfo(name = "UserID") var UserID: String?,
    @ColumnInfo(name = "CustomerID") var CustomerID: String?,
    @ColumnInfo(name = "EquipmentID") var EquipmentID: String?
)