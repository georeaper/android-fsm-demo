package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Settings")
data class Settings(
    @PrimaryKey var SettingsID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "RemoteID") var RemoteID: Int?,
    @ColumnInfo(name = "SettingsKey") var SettingsKey: String?,
    @ColumnInfo(name = "SettingsValue") var SettingsValue: String?,
    @ColumnInfo(name = "SettingsStyle") var SettingsStyle: String?,
    @ColumnInfo(name ="SettingsDescription") var SettingsDescription : String ? ,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "Version") var Version: String?
)
