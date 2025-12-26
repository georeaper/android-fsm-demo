package com.gkprojects.cmmsandroidapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gkprojects.cmmsandroidapp.data.local.CMMSDatabase
import com.gkprojects.cmmsandroidapp.data.local.dao.CustomerDao
import com.gkprojects.cmmsandroidapp.data.local.dao.EquipmentsDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FgnKeyTest {
    private lateinit var db: CMMSDatabase
    private lateinit var customerDao: CustomerDao
    private lateinit var equipmentDao: EquipmentsDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, CMMSDatabase::class.java).build()
        customerDao = db.CustomerDao()
        equipmentDao = db.EquipmentsDAO()
    }

    @After
    fun closeDb() {
        db.close()
    }


}