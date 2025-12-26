package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.TicketHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoTicketHistory {
    companion object {
        var userDatabase: CMMSDatabase? = null
        private fun initialiseDB(context: Context): CMMSDatabase? {
            return CMMSDatabase.getInstance(context)!!
        }

        suspend fun insertTicketHistory(context: Context, ticketHistory: TicketHistory) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            ticketHistory.DateCreated = now
            ticketHistory.LastModified = now
            userDatabase = initialiseDB(context)
            userDatabase!!.TicketHistoryDao().insertTicketHistory(ticketHistory)

        }

        suspend fun updateTicketHistory(context: Context, ticketHistory: TicketHistory) {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            ticketHistory.LastModified = now
            userDatabase = initialiseDB(context)
            userDatabase!!.TicketHistoryDao().updateTicketHistory(ticketHistory)


        }

        suspend fun deleteTicketHistory(context: Context, ticketHistory: TicketHistory) {
            userDatabase = initialiseDB(context)
            userDatabase!!.TicketHistoryDao().deleteTicketHistory(ticketHistory)

        }
        suspend fun getAllTicketHistory(context: Context): LiveData<List<TicketHistory>> {
            userDatabase = initialiseDB(context)
            return userDatabase!!.TicketHistoryDao().getAllTicketHistory()
        }
        suspend fun getAllListForSync(context: Context): List<TicketHistory> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.TicketHistoryDao().getAllTicketHistoryList() // This should be a suspend DAO function that returns List<Customer>
            }
        }

        suspend fun insertOrUpdate(context: Context, ticketHistory: List<TicketHistory>) {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                ticketHistory.forEach { ticketHistory ->
                    val existing = userDatabase!!.TicketHistoryDao().getTicketHistoryByIDNow(ticketHistory.TicketHistoryID) // This should be suspend.getCustomerByIDNow(customer.CustomerID) // This should be suspend
                    if (existing == null) {
                        userDatabase!!.TicketHistoryDao().insertTicketHistory(ticketHistory)
                    } else {
                        userDatabase!!.TicketHistoryDao().updateTicketHistory(ticketHistory)
                    }
                }
            }
        }
    }
}