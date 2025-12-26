package com.gkprojects.cmmsandroidapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "Tasks",
    foreignKeys = [
        ForeignKey(entity = Tickets::class,
            childColumns = ["TicketID"],
            parentColumns = ["TicketID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE),
        ForeignKey(entity = Users::class,
            childColumns = ["UserID"],
            parentColumns = ["UserID"],
            onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)
    ]
)
data class Tasks(
    @PrimaryKey var TaskID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "Title") var Title: String?,
    @ColumnInfo(name = "Description") var Description: String?,
    @ColumnInfo(name = "Status") var Status: String?, // E.g., "Pending", "In Progress", "Completed"
    @ColumnInfo(name = "Priority") var Priority: String?, // E.g., "Low", "Medium", "High"
    @ColumnInfo(name = "DateStart") var DateStart: String?,
    @ColumnInfo(name = "DateDue") var DateDue: String?,
    @ColumnInfo(name = "DateCompleted") var DateCompleted: String?,
    @ColumnInfo(name = "LastModified") var LastModified: String?,
    @ColumnInfo(name = "DateCreated") var DateCreated: String?,
    @ColumnInfo(name = "TicketID") var TicketID: String?, // Links task to a ticket
    @ColumnInfo(name = "UserID") var UserID: String? // Assign task to a user
)

