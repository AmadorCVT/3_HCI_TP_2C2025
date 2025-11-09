package com.example.listi.services

import com.example.listi.LoginRequest
import com.example.listi.LoginResponse
import com.example.listi.RegisterRequest
import com.example.listi.RegisterResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/users/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/api/users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}
