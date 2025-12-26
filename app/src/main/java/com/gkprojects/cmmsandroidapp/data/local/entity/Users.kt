package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Users")
data class Users(
    @PrimaryKey var UserID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "Name") var Name: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "Email") var Email: String?,
    @ColumnInfo(name = "Phone") var Phone: String?,
    @ColumnInfo(name = "Signature") var Signature: ByteArray?,
    @ColumnInfo(name = "PrefixWO") var ReportPrefix: String?,
    @ColumnInfo(name = "PrefixTC") var TechnicalCasePrefix: String?,
    @ColumnInfo(name = "LastReportNumber") var LastReportNumber: Int?,
    @ColumnInfo(name = "LastTCNumber") var LastTCNumber: Int?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)