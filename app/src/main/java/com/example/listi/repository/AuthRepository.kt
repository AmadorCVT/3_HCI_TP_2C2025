package com.example.listi.repository

import android.content.Context
import android.util.Log
import com.example.listi.network.RetrofitInstance
import com.example.listi.TokenManager
import com.example.listi.ui.types.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val service = RetrofitInstance.loginService
    private val tokenManager = TokenManager(context)


    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val response = service.loginUser(LoginRequest(email = email, password = password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.token.isNotBlank()) {
                    tokenManager.saveToken(body.token)
                }
                response
            } else {
                throw Exception(response.code().toString())
            }
        }
    }


    suspend fun register(firstName: String, lastName: String, email: String, password: String): Response<RegisterResponse> {
        val request = RegisterRequest(
            name = firstName,
            surname = lastName,
            email = email,
            password = password,
            metadata = emptyMap()
        )
        Log.d("RegisterScreen", "REGISTER")

        return try {
            val response = service.registerUser(request)
            Log.d("RegisterScreen", "Response recibida correctamente")
            response
        } catch (e: Exception) {
            Log.e("RegisterScreen", "Error dentro de service.registerUser()", e)
            throw e
        }
    }


    suspend fun verifyAccount(code: String): Response<VerifyAccountResponse> {
        val request = VerifyAccountRequest(code = code)
        return service.verifyAccount(request)
    }


    suspend fun sendVerificationCode(email: String): Response<SendVerificationResponse> {
        return service.sendVerificationCode(email)
    }





    suspend fun forgotPassword(email: String): Response<Unit> {
        return service.forgotPassword(email)
    }

    suspend fun resetPassword(code: String, newPassword: String): Response<Unit> {
        val request = ResetPasswordRequest(code = code, password = newPassword)
        return service.resetPassword(request)
    }


    suspend fun changePassword(currentPassword: String, newPassword: String): Response<Unit> {
        val request = ChangePasswordRequest(currentPassword = currentPassword, newPassword = newPassword)
        try {
            // Log the serialized JSON so we can inspect the exact payload sent
            val json = Json.encodeToString(request)
            Log.d("AuthRepository", "changePassword request json: $json")
        } catch (_: Exception) {
            // ignore logging errors
        }
        return service.changePassword(request)
    }


    suspend fun logout(): Response<Unit> {
        val response = service.logout()
        if (response.isSuccessful) {
            tokenManager.clearToken()
        } else {
            throw Exception(response.code().toString())
        }
        return response
    }


    suspend fun getSavedToken(): String? = tokenManager.getToken()

    suspend fun saveTokenLocally(token: String) = tokenManager.saveToken(token)

    suspend fun clearSavedToken() = tokenManager.clearToken()


    suspend fun getProfile(): Response<GetUserResponse> {
        return service.getProfile()
    }

    suspend fun updateProfile(request: UpdateUserRequest): Response<Unit> {
        return service.updateProfile(request)
    }
}
