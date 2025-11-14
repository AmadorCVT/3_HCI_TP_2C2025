package com.example.listi.network


import com.example.listi.ui.types.LoginRequest
import com.example.listi.ui.types.LoginResponse
import com.example.listi.ui.types.RegisterRequest
import com.example.listi.ui.types.RegisterResponse
import com.example.listi.ui.types.ResetPasswordRequest
import com.example.listi.ui.types.SendVerificationResponse
import com.example.listi.ui.types.VerifyAccountRequest
import com.example.listi.ui.types.VerifyAccountResponse
import com.example.listi.ui.types.ChangePasswordRequest
import com.example.listi.ui.types.GetUserResponse
import com.example.listi.ui.types.UpdateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface LoginService {
    @POST("/api/users/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/api/users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/users/verify-account")
    suspend fun verifyAccount(@Body request: VerifyAccountRequest): Response<VerifyAccountResponse>

    @POST("/api/users/send-verification")
    suspend fun sendVerificationCode(@Query("email") email: String): Response<SendVerificationResponse>

    @POST("/api/users/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): Response<Unit>

    @POST("/api/users/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("/api/users/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Unit>

    @POST("/api/users/logout")
    suspend fun logout(): Response<Unit>

    @GET("/api/users/profile")
    suspend fun getProfile(): Response<GetUserResponse>

    @PUT("/api/users/profile")
    suspend fun updateProfile(@Body request: UpdateUserRequest): Response<Unit>
}
