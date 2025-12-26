package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Contracts",
    foreignKeys = [ForeignKey(entity = Customer::class,
        childColumns = ["CustomerID"],
        parentColumns = ["CustomerID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE)]
)
data class Contracts(
    @PrimaryKey var ContractID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Title") var Title: String?,
    @ColumnInfo(name = "DateStart") var DateStart: String?,
    @ColumnInfo(name = "DateEnd") var DateEnd: String?,
    @ColumnInfo(name = "Value") var Value: Double?,
    @ColumnInfo(name = "Notes") var Notes: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "ContractType") var ContractType: String?,
    @ColumnInfo(name = "ContractStatus") var ContractStatus: Boolean?,
    @ColumnInfo(name = "ContactName") var ContactName: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?,
    @ColumnInfo(name = "CustomerID") var CustomerID: String?
)