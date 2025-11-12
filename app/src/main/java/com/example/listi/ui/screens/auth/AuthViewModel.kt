package com.example.listi.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.repository.AuthRepository
import kotlinx.coroutines.launch


class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    // --------------------------
    // LOGIN
    // --------------------------
    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    uiState = uiState.copy(
                        isLogged = true,
                        token = token
                    )
                } else {
                    uiState = uiState.copy(errorMessage = "Credenciales inválidas")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // --------------------------
    // REGISTER
    // --------------------------
    fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = authRepository.register(firstName, lastName, email, password)
                if (response.isSuccessful) {
                    uiState = uiState.copy(showVerification = true)
                } else {
                    uiState = uiState.copy(errorMessage = "Error al registrar usuario")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // --------------------------
    // VERIFICAR CUENTA
    // --------------------------
    fun verifyAccount(code: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val response = authRepository.verifyAccount(code)
                if (response.isSuccessful) {
                    uiState = uiState.copy(isVerified = true)
                } else {
                    uiState = uiState.copy(errorMessage = "Código incorrecto")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // --------------------------
    // REENVIAR CÓDIGO
    // --------------------------
    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                authRepository.sendVerificationCode(email)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "No se pudo reenviar el código")
            }
        }
    }

    // --------------------------
    // RESET PASSWORD
    // --------------------------
    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val response = authRepository.resetPassword(code, newPassword)
                if (response.isSuccessful) {
                    uiState = uiState.copy(passwordChanged = true)
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo restablecer la contraseña")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    // --------------------------
    // LOGOUT
    // --------------------------
    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {
                // no importa mucho el error de logout en cliente
            } finally {
                uiState = AuthUiState() // reinicia el estado
            }
        }
    }
}
