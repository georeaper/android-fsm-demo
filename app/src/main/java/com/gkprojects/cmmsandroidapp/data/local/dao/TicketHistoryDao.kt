package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.TicketHistory

@Dao
interface TicketHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketHistory(ticketHistory: TicketHistory)

    @Update
    suspend fun updateTicketHistory(ticketHistory: TicketHistory)

    @Delete
    suspend fun deleteTicketHistory(ticketHistory: TicketHistory)

    @Query ("SELECT * FROM ticket_history")
    fun getAllTicketHistory(): LiveData<List<TicketHistory>>

    @Query("SELECT * FROM ticket_history")
    suspend fun getAllTicketHistoryList(): List<TicketHistory> // <--- For sync

    @Query("SELECT * FROM ticket_history WHERE TicketHistoryID = :id LIMIT 1")
    suspend fun getTicketHistoryByIDNow(id: String): TicketHistory? // <--- For sync
}