package com.example.listi.ui.types

import kotlinx.serialization.Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val metadata: Map<String, Any> = emptyMap()
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, Any>,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class LoginResponse(
    val token: String
)

data class VerifyAccountRequest(
    val code: String
)

data class VerifyAccountResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, Any>?,
    val updatedAt: String,
    val createdAt: String
)

data class ResetPasswordRequest(
    val code: String,
    val password: String
)

data class SendVerificationResponse(
    val code: String
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
