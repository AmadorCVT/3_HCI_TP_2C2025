package com.example.listi.repository

import android.content.Context
import com.example.listi.network.RetrofitInstance
import com.example.listi.TokenManager
import com.example.listi.ui.types.*
import retrofit2.Response

/**
 * Repo de autenticación.
 *
 * - Devuelve los Response<T> de Retrofit para que el ViewModel pueda inspeccionar estados y cuerpos.
 * - Guarda / elimina token en TokenManager cuando corresponde.
 */
class AuthRepository(private val context: Context) {

    private val service = RetrofitInstance.loginService
    private val tokenManager = TokenManager(context)

    // -------------------
    // LOGIN
    // -------------------
    /**
     * Llama al endpoint de login y guarda el token si la respuesta es exitosa y viene en el body.
     * Devuelve el Response<LoginResponse> tal cual lo devuelve Retrofit.
     */
    suspend fun login(email: String, password: String): Response<LoginResponse> {

        val response = service.loginUser(LoginRequest(email = email, password = password))

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.token.isNotBlank()) {
                tokenManager.saveToken(body.token)
            }
        }
        return response
    }

    // -------------------
    // REGISTER
    // -------------------
    /**
     * Registra un usuario. Devuelve el Response<RegisterResponse>.
     */
    suspend fun register(firstName: String, lastName: String, email: String, password: String): Response<RegisterResponse> {
        val request = RegisterRequest(
            name = firstName,
            surname = lastName,
            email = email,
            password = password,
            metadata = emptyMap()
        )
        return service.registerUser(request)
    }

    // -------------------
    // VERIFY ACCOUNT
    // -------------------
    suspend fun verifyAccount(code: String): Response<VerifyAccountResponse> {
        val request = VerifyAccountRequest(code = code)
        return service.verifyAccount(request)
    }

    // -------------------
    // SEND VERIFICATION CODE
    // -------------------
    suspend fun sendVerificationCode(email: String): Response<SendVerificationResponse> {
        return service.sendVerificationCode(email)
    }

    // -------------------
    // FORGOT / RESET
    // -------------------
    suspend fun forgotPassword(email: String): Response<Unit> {
        return service.forgotPassword(email)
    }

    suspend fun resetPassword(code: String, newPassword: String): Response<Unit> {
        val request = ResetPasswordRequest(code = code, password = newPassword)
        return service.resetPassword(request)
    }

    // -------------------
    // CHANGE PASSWORD
    // -------------------
    suspend fun changePassword(currentPassword: String, newPassword: String): Response<Unit> {
        val request = ChangePasswordRequest(currentPassword = currentPassword, newPassword = newPassword)
        return service.changePassword(request)
    }

    // -------------------
    // LOGOUT
    // -------------------
    /**
     * Llama al endpoint de logout. Si la respuesta es exitosa, borra el token local.
     */
    suspend fun logout(): Response<Unit> {
        val response = service.logout()
        if (response.isSuccessful) {
            tokenManager.clearToken()
        }
        return response
    }

    // -------------------
    // TOKEN HELPERS
    // -------------------
    suspend fun getSavedToken(): String? = tokenManager.getToken()

    suspend fun saveTokenLocally(token: String) = tokenManager.saveToken(token) // sync helper

    suspend fun clearSavedToken() = tokenManager.clearToken()
}
