package com.gkprojects.cmmsandroidapp.domain.usecase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationService private constructor(private val context: Context) {

    private val notifications = mutableListOf<Notification>()
    private val filename = "notifications.json"

    private val _unseenCount = MutableLiveData<Int>(0)
    val unseenCount: LiveData<Int> get() = _unseenCount

    init {
        loadNotifications()
        updateUnseenCount()
    }

    fun addNotification(notification: Notification) {
        notifications.add(notification)
        saveNotifications()
        updateUnseenCount()
    }

    fun markAllAsSeen() {
        notifications.forEach { it.seen = true }
        saveNotifications()
        updateUnseenCount()
    }

    private fun loadNotifications() {
        try {
            val file = File(context.filesDir, filename)
            if (file.exists()) {
                val json = file.readText()
                val listType = object : TypeToken<List<Notification>>() {}.type
                Log.d("badgeLog2","$notifications")
                notifications.clear()
                notifications.addAll(Gson().fromJson(json, listType))
                Log.d("badgeLog3","$notifications")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getNotifications(): List<Notification> {
        val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault())

        return notifications.sortedByDescending {
            try {
                dateFormat.parse(it.timeStamp) // Parse the timestamp to Date
            } catch (e: Exception) {
                e.printStackTrace()
                null // Handle invalid dates by treating them as null
            }
        }
    }
    fun deleteAllNotifications() {
        val newNotificationList = notifications.filter { !it.seen }
        notifications.clear()
        notifications.addAll(newNotificationList)
        saveNotifications()
        updateUnseenCount()
    }

    fun markAsSeen(id : Int){
        val notification = notifications.find { it.id == id }
        notification?.let {
            it.seen = true
            saveNotifications()
            updateUnseenCount()
        }
    }

    private fun saveNotifications() {
        try {
            val json = Gson().toJson(notifications)
            val file = File(context.filesDir, filename)
            FileWriter(file).use { it.write(json) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun updateUnseenCount() {
        val count = notifications.count { !it.seen }
        _unseenCount.postValue(count) // Use postValue for background threads
    }

    companion object {
        private var instance: NotificationService? = null

        fun getInstance(context: Context): NotificationService {
            if (instance == null) {
                instance = NotificationService(context.applicationContext)
            }
            return instance!!
        }
    }
}
