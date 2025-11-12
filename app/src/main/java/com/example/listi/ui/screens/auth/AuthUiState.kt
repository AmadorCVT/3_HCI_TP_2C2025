package com.example.listi.ui.screens.auth

import com.example.listi.ui.types.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val isVerified: Boolean = false,
    val passwordChanged: Boolean = false,
    val showVerification: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val currentUser: User? = null,
)