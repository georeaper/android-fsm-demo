package com.gkprojects.cmmsandroidapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

interface SyncApi {
    @POST("sync/fetchEntities")
    suspend fun fetchEntities(
        @Body request: ApiRequest
    ): List<Any> // This will return a dynamic list, cast later
}
data class ApiRequest(
    val entityName: String, // e.g., "User", "Product"
    val timestamp: String   // e.g., "2024-03-03T12:00:00Z"
)



