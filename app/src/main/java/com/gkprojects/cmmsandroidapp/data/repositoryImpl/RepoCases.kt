package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.dto.OverviewMainData
import com.gkprojects.cmmsandroidapp.data.local.dto.TicketCalendar
import com.gkprojects.cmmsandroidapp.data.local.dto.TicketCustomerName
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoCases {

    companion object{
        var userDatabase: CMMSDatabase?=null

        private fun initialiseDB(context: Context): CMMSDatabase?
        {
            return CMMSDatabase.getInstance(context)!!
        }

        suspend fun insert(context: Context, tickets: Tickets)
        {
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)
            tickets.DateCreated = now
            tickets.LastModified=now
            userDatabase = initialiseDB(context)
            userDatabase!!.TicketsDao().addTickets(tickets)

        }
        suspend fun getItemByIDForDeletion(context: Context,id: String): Tickets? {
            userDatabase = initialiseDB(context)
            return userDatabase!!.TicketsDao().getTicketsByIDNow(id)
        }
//
        fun delete(context: Context, tickets: Tickets){
            userDatabase = initialiseDB(context)
            CoroutineScope(Dispatchers.IO).launch {

                userDatabase!!.TicketsDao().deleteTickets(tickets)
            }

        }
//
        fun getAllCustomerData(context: Context): LiveData<List<Tickets>>
        {
            userDatabase = initialiseDB(context)
            //return userDatabase!!.hospitalDAO().getAllHospitals()
            return userDatabase!!.TicketsDao().getAllTickets()
        }
//
        fun updateCustomerData(context: Context, tickets: Tickets){
            val currentDateTime = Calendar.getInstance().time
            val now = DateUtils.format(currentDateTime)

            tickets.LastModified=now
            userDatabase = initialiseDB(context)
                CoroutineScope(Dispatchers.IO).launch {
                    userDatabase!!.TicketsDao().updateTickets(tickets)
                }

        }
        fun getCustomerDataByID(context: Context,id:String):LiveData<Tickets>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.TicketsDao().getTicketsById(id)
        }
        fun getCustomerIdData(context: Context): LiveData<List<CustomerSelect>> {

            //return userDatabase!!.hospitalDAO().getIdFromHospital()
            return userDatabase!!.TicketsDao().getCustomerID()

        }
        @SuppressLint("SuspiciousIndentation")
        fun getCustomerNameTickets(context: Context):LiveData<List<TicketCustomerName>>{

            userDatabase = initialiseDB(context)
                return userDatabase!!.TicketsDao().getCustomerName()

        }

        fun getDataForHome(context: Context):LiveData<List<OverviewMainData>>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.TicketsDao().getDateForOverview()
        }
        fun getInformationCasesCalendar(context: Context):LiveData<List<TicketCalendar>>{
            userDatabase = initialiseDB(context)
            return userDatabase!!.TicketsDao().getTicketInformationCalendar()
        }
        // Sync function bellow

        suspend fun getAllListForSync(context: Context): List<Tickets> {
            userDatabase = initialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.TicketsDao().getAllTicketsList()  // This should be a suspend DAO function that returns <List<Departments>>
            }
        }

        suspend fun insertOrUpdate(context: Context, tickets:  List<Tickets>)  {
            userDatabase = initialiseDB(context)
            withContext(Dispatchers.IO) {
                tickets.forEach { tickets ->
                    val existing = userDatabase!!.TicketsDao().getTicketsByIDNow(tickets.TicketID)// This should be suspend
                    if (existing == null) {
                        userDatabase!!.TicketsDao().addTickets(tickets)
                    } else {
                        userDatabase!!.TicketsDao().updateTickets(tickets)
                    }
                }
            }
        }

    }
}