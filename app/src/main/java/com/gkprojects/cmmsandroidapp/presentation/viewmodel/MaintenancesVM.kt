package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoMaintenances

class MaintenancesVM :ViewModel(){
    private lateinit var notificationService: NotificationService
    fun insert(context : Context,maintenances: Maintenances){
        notificationService = NotificationService.getInstance(context)
        RepoMaintenances.insert(context,maintenances)
        notificationService.addNotification(
            Notification(
                1,
                timeStamp = maintenances.LastModified.toString(),
                title = "New Maintenance",
                description = "Maintenance : ${maintenances.Name} has been added",
                type = "Added",
                function = "None",
                seen = false)
        )

    }
    suspend fun update(context : Context,maintenances: Maintenances){
        notificationService = NotificationService.getInstance(context)
        RepoMaintenances.update(context,maintenances)
        notificationService.addNotification(
            Notification(
                1,
                timeStamp = maintenances.LastModified.toString(),
                title = "Maintenance update",
                description = "Maintenance : ${maintenances.Name} has been updated",
                type = "Updated",
                function = "None",
                seen = false)
        )
    }


    suspend fun delete(context : Context, maintenances: Maintenances){
        RepoMaintenances.delete(context ,maintenances)

    }

    fun getAllMaintenances(context:Context):LiveData<List<Maintenances>>{

        return  RepoMaintenances.getAllMaintenances(context)

    }
}