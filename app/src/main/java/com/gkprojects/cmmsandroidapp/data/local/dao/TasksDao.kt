package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gkprojects.cmmsandroidapp.data.local.entity.Tasks


@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
     fun getAll(): LiveData<List<Tasks>>
    @Query("SELECT * FROM tasks WHERE userId =:userId")
    suspend fun loadAllByUser(userId: String): List<Tasks>
    @Update
    suspend fun update(task: Tasks)
    @Insert
    suspend fun insert(tasks: Tasks)
    @Delete
    suspend fun delete(task: Tasks)

    @Query("SELECT * FROM Tasks")
    suspend fun getAllTasksList(): List<Tasks> // <--- For sync

    @Query("SELECT * FROM Tasks WHERE TaskID = :id LIMIT 1")
    suspend fun getTasksByIDNow(id: String): Tasks? // <--- For sync



}