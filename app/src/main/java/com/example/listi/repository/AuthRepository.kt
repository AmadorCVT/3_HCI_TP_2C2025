package com.example.listi.repository

import android.content.Context
import com.example.listi.RetrofitInstance
import com.example.listi.TokenManager
import com.example.listi.ui.types.LoginRequest
import com.example.listi.ui.types.RegisterRequest

class AuthRepository(private val context: Context) {

    private val tokenManager = TokenManager(context)

    suspend fun login(email: String, password: String): Boolean {
        val response = RetrofitInstance.loginService.loginUser(LoginRequest(email, password))
        return if (response.isSuccessful && response.body() != null) {
            val token = response.body()!!.token
            tokenManager.saveToken(token)
            true
        } else {
            false
        }
    }

    suspend fun getSavedToken(): String? {
        return tokenManager.getToken()
    }
    suspend fun register(firstName: String, lastName: String, email: String, password: String): Boolean {
        val response = RetrofitInstance.loginService.registerUser(
            RegisterRequest(firstName, lastName, email, password)
        )
        return response.isSuccessful
    }
}