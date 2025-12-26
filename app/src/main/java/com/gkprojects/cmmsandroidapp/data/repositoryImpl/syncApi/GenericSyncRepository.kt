package com.gkprojects.cmmsandroidapp.data.repositoryImpl.syncApi

import android.util.Log
import com.gkprojects.cmmsandroidapp.data.remote.api.ApiRequest
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncApi
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.IOException

class GenericSyncRepository(private val apiService: SyncApi) {
    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }



    suspend fun <T : Any> fetchEntities(
        request: ApiRequest,
        entityType: Class<T>
    ): Result<List<T>> {
        return try {
            val response = apiService.fetchEntities(request)
            Log.d("Response", response.toString())
            val list = response.mapNotNull { item ->
                try {
                    Log.d("Response", item.toString())
                    Gson().fromJson(Gson().toJson(item), entityType)
                } catch (e: JsonSyntaxException) {
                    // Handle JSON parsing errors
                    null
                }
            }
            Result.Success(list)
        } catch (e: IOException) {
            // Handle network-related errors
            Result.Error(e)
        } catch (e: Exception) {
            // Handle other unexpected errors
            Result.Error(e)
        }
    }
}