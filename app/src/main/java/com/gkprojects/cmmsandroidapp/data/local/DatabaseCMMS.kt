package com.gkprojects.cmmsandroidapp.data.local

import android.content.Context
import android.util.Log
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.CategoryDao
import com.gkprojects.cmmsandroidapp.data.local.dao.CheckFormsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.ContractEquipmentsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.ContractsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.CustomerDao
import com.gkprojects.cmmsandroidapp.data.local.dao.DepartmentsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.EquipmentsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportCheckFormsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportEquipmentDao
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.MaintenancesDao
import com.gkprojects.cmmsandroidapp.data.local.dao.ManufacturerDao
import com.gkprojects.cmmsandroidapp.data.local.dao.ModelDao
import com.gkprojects.cmmsandroidapp.data.local.dao.SettingsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.SynchronizationDao
import com.gkprojects.cmmsandroidapp.data.local.dao.TasksDao
import com.gkprojects.cmmsandroidapp.data.local.dao.TicketHistoryDao
import com.gkprojects.cmmsandroidapp.data.local.dao.TicketsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.UsersDao
import com.gkprojects.cmmsandroidapp.data.local.dao.InventoryDao
import com.gkprojects.cmmsandroidapp.data.local.dao.ToolsDao
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportInventoryDao
import com.gkprojects.cmmsandroidapp.data.local.dao.FieldReportToolsDao
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset
import com.gkprojects.cmmsandroidapp.data.local.entity.CheckForms
import com.gkprojects.cmmsandroidapp.data.local.entity.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Contracts
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.entity.Departments
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings
import com.gkprojects.cmmsandroidapp.data.local.entity.Synchronization
import com.gkprojects.cmmsandroidapp.data.local.entity.Tasks
import com.gkprojects.cmmsandroidapp.data.local.entity.TicketHistory
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.data.local.entity.Users

@Database(
    entities = [Customer::class,
        Inventory::class,
        Contracts::class,
        ContractEquipments::class,
        Departments::class,
        Equipments::class,
        FieldReportEquipment::class,
        FieldReportInventory::class,
        FieldReports::class,
        Maintenances::class,
        Manufacturer::class,
        ModelAsset::class,
        CategoryAsset::class,
        Tickets::class,
        Users::class,
        CheckForms::class,
        Settings::class,
        Tools::class,
        FieldReportCheckForm::class,
        FieldReportTools::class,
        Synchronization::class,
        Tasks::class,
        TicketHistory::class
               ],
    version =1,
    exportSchema = true
//   ,autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//       ,AutoMigration (from = 2, to = 3)
//       ,AutoMigration (from = 3, to = 4)
//       ,AutoMigration (from = 4, to = 5)
//     ]
)
abstract class CMMSDatabase : RoomDatabase() {

    abstract fun CustomerDao(): CustomerDao
    abstract fun EquipmentsDAO(): EquipmentsDao
    abstract fun DepartmentsDao(): DepartmentsDao
    abstract fun ContractEquipmentsDao(): ContractEquipmentsDao
    abstract fun ContractsDao(): ContractsDao
    abstract fun FieldReportEquipmentDao(): FieldReportEquipmentDao
    abstract fun FieldReportsDao(): FieldReportsDao

    abstract fun InventoryDao(): InventoryDao

    abstract fun MaintenancesDao(): MaintenancesDao
    abstract fun TicketsDao(): TicketsDao //cases repository
    abstract fun UsersDao(): UsersDao
    abstract fun FieldReportInventoryDao(): FieldReportInventoryDao

    abstract fun FieldReportToolsDao(): FieldReportToolsDao //
    abstract fun ToolsDao(): ToolsDao
    abstract fun CheckFormsDao(): CheckFormsDao
    abstract fun FieldReportCheckFormsDao(): FieldReportCheckFormsDao //repo checkList items
    abstract fun SettingsDao(): SettingsDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun ModelDao(): ModelDao
    abstract fun ManufacturerDao(): ManufacturerDao
    abstract fun SynchronizationDao(): SynchronizationDao
    abstract fun TasksDao(): TasksDao
    abstract fun TicketHistoryDao(): TicketHistoryDao



       companion object  {
       @Volatile
       private var instance: CMMSDatabase? = null

       //private const val DATABASE_NAME="cmmsAppDB11022024b"
       //private const val DATABASE_NAME="cmmsAppDB19032024_b" //ID because UUID Strings
       fun getInstance(context: Context): CMMSDatabase?
       {
           if(instance == null)
           {
               synchronized(CMMSDatabase::class.java)
               {
                   if (instance == null) {
                       val sharedPreferences = context.getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)
                       val dbName = sharedPreferences.getString("dbName", "defaultDbName") ?: "defaultDbName"
                       Log.d("dbName", dbName)

                       instance = Room.databaseBuilder(context, CMMSDatabase::class.java, dbName)
                           .fallbackToDestructiveMigration()
                           .addCallback(object :RoomDatabase.Callback(){
                               override fun onOpen(db: SupportSQLiteDatabase) {
                                   super.onOpen(db)
                                   db.execSQL("PRAGMA foreign_keys = ON")
                               }
                           })
                           .build()
                   }
               }
           }

           return instance
       }


    }

}

