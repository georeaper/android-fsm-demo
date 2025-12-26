package com.gkprojects.cmmsandroidapp.data.remote.datasource

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi.SyncViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class SyncManager(
    private val syncViewModel: SyncViewModel,
    //private val synchronizationViewModel: SynchronizationViewmodel
) {

    //private val apiService = ApiClient().createService(SyncApi::class.java)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    fun <T> startSync(entity: String, entityType: Class<T>) {
        CoroutineScope(Dispatchers.IO).launch {
            val lastSyncTimestamp = "01/01/2010 00:00:00"
            syncViewModel.fetchEntityData(entity, lastSyncTimestamp, entityType)
        }
    }


}