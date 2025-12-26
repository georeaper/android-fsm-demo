package com.gkprojects.cmmsandroidapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

interface SyncData {
    @POST("sync/syncData")
    suspend fun syncData(@Body request: SyncDataRequest): SyncDataResponse

}

data class SyncDataRequest(
    val entityName: String, // e.g., "User", "Product"
    val timestamp: String ,  // e.g., "2024-03-03T12:00:00Z"
    val data: List<Any> // This will be a dynamic list, cast later eg . List <Customers> , List<Equipments> , List<WorkOrders
)
data class SyncDataResponse(
    val entityName: String, // e.g., "User", "Product"
    val timestamp: String ,  // e.g., "2024-03-03T12:00:00Z"
    val data: List<Any> // This will be a dynamic list, cast later eg . List <Customers> , List<Equipments> , List<WorkOrders
)