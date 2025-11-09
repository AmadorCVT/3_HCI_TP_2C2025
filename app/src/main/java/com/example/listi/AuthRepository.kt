package com.example.listi


import android.content.Context
import com.example.listi.LoginRequest
import com.example.listi.RetrofitInstance
import com.example.listi.TokenManager

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
}
