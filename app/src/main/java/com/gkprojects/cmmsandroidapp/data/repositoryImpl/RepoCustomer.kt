package com.gkprojects.cmmsandroidapp.data.repositoryImpl

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerContractsDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerEquipmentDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerTechnicalCasesDataClass

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepoCustomer {


    companion object{
        var userDatabase: CMMSDatabase?=null

        private fun intialiseDB(context: Context): CMMSDatabase?
        {
            return CMMSDatabase.getInstance(context)!!
        }

        suspend fun insert(context: Context, customer : Customer)
        {
            val currentDateTime = Calendar.getInstance().time

            // Use DateUtils to get the standardized date string
            val now = DateUtils.format(currentDateTime)
            customer.DateCreated = now
            customer.LastModified = now
            val db = intialiseDB(context)!!

            try {
                db.CustomerDao().addCustomer(customer)
            }catch (e:SQLiteConstraintException){
                throw SQLiteConstraintException("Customer already exists")
            }

        }
        suspend fun delete(context: Context, customer: Customer) {
            val db = intialiseDB(context)!!
            try {

                db.CustomerDao().deleteCustomer(customer)
            } catch (e: SQLiteConstraintException) {
                // Throw back with a clear message
                throw SQLiteConstraintException("Cannot delete customer ,is connected with other table.")
            }
        }
        @SuppressLint("SuspiciousIndentation")
        suspend fun update(context: Context, customer: Customer) {
            val currentDateTime = Calendar.getInstance().time

            // Use DateUtils to get the standardized date string
            val now = DateUtils.format(currentDateTime)
            customer.LastModified = now

            val db = intialiseDB(context)!!

                try {
                    db.CustomerDao().updateCustomer(customer)
                } catch (e: Exception) {
                    Log.e("Update Error", "Failed to update customer", e)
                }

        }


        fun getCustomerID(context :Context ,id :String):LiveData<Customer>{
            userDatabase = intialiseDB(context)
            return userDatabase!!.CustomerDao().getCustomerByID(id)
        }




        fun getAllCustomerData(context: Context): LiveData<List<Customer>>
        {
            userDatabase = intialiseDB(context)
            return userDatabase!!.CustomerDao().getAllCustomer()
        }


        fun getEquipmentDashboard(context: Context, id :String):LiveData<List<DashboardCustomerEquipmentDataClass>>{
            userDatabase = intialiseDB(context)
            return userDatabase!!.CustomerDao().getDashboardEquipmentsByID(id)

        }
        fun getContractsDashboard(context: Context, id: String):LiveData<List<DashboardCustomerContractsDataClass>>{
            userDatabase = intialiseDB(context)
            return userDatabase!!.CustomerDao().getDashboardContractsByID(id)
        }
        fun getTechnicalCasesDashboard(context: Context, id:String):LiveData<List<DashboardCustomerTechnicalCasesDataClass>>{
            userDatabase = intialiseDB(context)
            return userDatabase!!.CustomerDao().getDashboardTechnicalCaseByID(id)
        }
        // syncing data functions ,ETC
        //

        suspend fun insertSync (context: Context,customer: Customer)
        {
            val db = intialiseDB(context)!!
            try {
                db.CustomerDao().addCustomer(customer)
            }catch (e:SQLiteConstraintException){
                throw SQLiteConstraintException("Customer already exists")
            }

        }
        suspend fun updateSync(context: Context, customer: Customer){
            userDatabase = intialiseDB(context)

            try {
                userDatabase!!.CustomerDao().updateCustomer(customer)
            } catch (e: Exception) {
                Log.e("Update Error", "Failed to update customer", e)
            }

        }
        suspend fun getAllCustomersForSync(context: Context): List<Customer> {
            userDatabase = intialiseDB(context)
            return withContext(Dispatchers.IO) {
                userDatabase!!.CustomerDao().getAllCustomerList() // This should be a suspend DAO function that returns List<Customer>
            }
        }

        suspend fun insertOrUpdateCustomers(context: Context, customers: List<Customer>) {
            userDatabase = intialiseDB(context)
            withContext(Dispatchers.IO) {
                customers.forEach { customer ->
                    val existing = userDatabase!!.CustomerDao().getCustomerByIDNow(customer.CustomerID) // This should be suspend
                    if (existing == null) {
                        userDatabase!!.CustomerDao().addCustomer(customer)
                    } else {
                        userDatabase!!.CustomerDao().updateCustomer(customer)
                    }
                }
            }
        }

    }

}