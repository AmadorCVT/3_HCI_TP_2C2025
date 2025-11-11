package com.example.listi.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listi.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var uiState by mutableStateOf(AuthUIState())
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val success = authRepository.login(email, password)
                uiState = if (success) {
                    uiState.copy(isLogged = true)
                } else {
                    uiState.copy(errorMessage = "Credenciales inválidas")
                }
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun goToRegister() {
        uiState = uiState.copy(showRegister = true)
    }

    fun goToLogin() {
        uiState = uiState.copy(showRegister = false)
    }
}

data class AuthUIState(
    val isLoading: Boolean = false,
    val isLogged: Boolean = false,
    val showRegister: Boolean = false,
    val errorMessage: String? = null
)
