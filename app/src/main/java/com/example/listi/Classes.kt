package com.example.listi

data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val metadata: Map<String, Any> = emptyMap()
)

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

data class LoginResponse(
    val token: String
)

annotation class Classes
