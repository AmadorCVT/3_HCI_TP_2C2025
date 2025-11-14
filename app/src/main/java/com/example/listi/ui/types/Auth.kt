package com.example.listi.ui.types

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, String> = emptyMap(),
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class VerifyAccountRequest(
    val code: String
)

@Serializable
data class VerifyAccountResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, String>? = null,
    val updatedAt: String,
    val createdAt: String
)

@Serializable
data class ResetPasswordRequest(
    val code: String,
    val password: String
)

@Serializable
data class SendVerificationResponse(
    val code: String
)

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

@Serializable
data class GetUserResponse(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val metadata: Map<String, String>? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val surname: String? = null,
    val metadata: Map<String, String>? = null
)
