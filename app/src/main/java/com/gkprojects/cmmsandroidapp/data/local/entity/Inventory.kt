package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "Inventory")
data class Inventory(
    @PrimaryKey var InventoryID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Title") var Title: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "Quantity") var Quantity: Long?,
    @ColumnInfo(name = "Value") var Value: Double?,
    @ColumnInfo(name = "Type") var Type: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)