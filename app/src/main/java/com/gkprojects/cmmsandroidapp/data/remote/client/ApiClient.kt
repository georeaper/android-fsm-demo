package com.gkprojects.cmmsandroidapp.data.remote.client
import com.gkprojects.cmmsandroidapp.data.remote.interceptor.AuthInterceptor
import com.gkprojects.cmmsandroidapp.data.remote.datasource.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val BASE_URL = "http://192.168.1.65:8080"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { TokenManager.getToken() }) // Attach token
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}