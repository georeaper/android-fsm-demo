package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Synchronization")
data class Synchronization(
    @PrimaryKey var SyncID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "Entity") var Entity: String?, // E.g., "Tickets", "Tasks", "Contracts"
    @ColumnInfo(name = "LastSyncDate") var LastSyncDate: String?, // Timestamp of the last sync
    @ColumnInfo(name = "Status") var Status: String?, // E.g., "Successful", "Failed"
    @ColumnInfo(name = "Notes") var Notes: String? // Additional details about the sync
)