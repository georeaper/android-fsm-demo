package com.gkprojects.cmmsandroidapp.data.remote.api

import com.gkprojects.cmmsandroidapp.data.remote.dto.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthLoginService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}

data class LoginRequest(val username: String, val password: String)