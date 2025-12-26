package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "Departments",
    foreignKeys = [ForeignKey(entity = Customer::class,
        childColumns = ["CustomerID"],
        parentColumns = ["CustomerID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
)
data class Departments(
    @PrimaryKey var DeparmentID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Name") var Name: String?,
    @ColumnInfo(name = "Phone") var Phone: String?,
    @ColumnInfo(name = "Email") var Email: String?,
    @ColumnInfo(name = "ContactPerson") var ContactPerson: String?,
    @ColumnInfo(name = "Notes") var Notes: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "DepartmentStatus") var DepartmentStatus: Boolean?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?,
    @ColumnInfo(name = "CustomerID") var CustomerID: String?
)