package com.example.listi.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.types.GetUserResponse
import com.example.listi.ui.types.User
import com.example.listi.ui.types.UpdateUserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    fun login(email: String, password: String) {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.login(email, password)
                if (!response.isSuccessful || response.body() == null) {
                    _errorMessage.value = "1"
                    return@launch
                }

                val token = response.body()!!.token

                uiState = uiState.copy(
                    isLogged = true,
                    token = token
                )

                loadProfile()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun register(firstName: String, lastName: String, email: String, password: String) {

        viewModelScope.launch {

            uiState = uiState.copy(isLoading = true,)
            try {

                val response = authRepository.register(firstName, lastName, email, password)

                if (response.isSuccessful) {
                    uiState = uiState.copy(showVerification = true,)

                } else {
                    uiState = uiState.copy(errorMessage = "Error al registrar usuario",)

                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }


    fun verifyAccount(code: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)
            try {
                val response = authRepository.verifyAccount(code)
                if (response.isSuccessful) {
                    uiState = uiState.copy(isVerified = true,)
                } else {
                    uiState = uiState.copy(errorMessage = "Código incorrecto",)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }


    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                authRepository.sendVerificationCode(email)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
                uiState = uiState.copy(errorMessage = e.message ?: "No se pudo reenviar el código",)
            }
        }
    }


    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)
            try {
                val response = authRepository.resetPassword(code, newPassword)
                if (response.isSuccessful) {
                    uiState = uiState.copy(passwordChanged = true,)
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo restablecer la contraseña",)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }

    fun sendForgotPasswordCode(email: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)

            try {
                val response = authRepository.forgotPassword(email)

                if (response.isSuccessful) {

                    uiState = uiState.copy(
                        forgotCodeSent = true
                    )
                } else {
                    _errorMessage.value = "2"
                    uiState = uiState.copy(
                        errorMessage = "No se pudo enviar el código de recuperación",
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    errorMessage = e.message ?: "Error de conexión",
                )
            } finally {
                uiState = uiState.copy()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {

            } finally {
                uiState = AuthUiState()
            }
        }
    }


    fun loadProfile() {
        viewModelScope.launch {
            try {
                val response = authRepository.getProfile()
                if (response.isSuccessful && response.body() != null) {
                    val body: GetUserResponse = response.body()!!
                    val user = User(
                        id = body.id,
                        name = body.name,
                        surname = body.surname,
                        email = body.email,
                        metadata = body.metadata,
                        createdAt = body.createdAt,
                        updatedAt = body.updatedAt
                    )
                    uiState = uiState.copy(currentUser = user, isLogged = true)
                } else {

                    uiState = uiState.copy(errorMessage = "No se pudo obtener perfil",)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión al obtener perfil",)
            }
        }
    }


    fun updateProfileMetadata(metadata: Map<String, String>) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)
            try {
                val current = uiState.currentUser
                val request = UpdateUserRequest(
                    name = current?.name,
                    surname = current?.surname,
                    metadata = metadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {

                    loadProfile()
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo actualizar perfil",)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al actualizar perfil",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }


    fun updateProfilePhoto(base64Data: String) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true,)

                val existing = uiState.currentUser?.metadata ?: emptyMap()
                val newMetadata = existing.toMutableMap()
                newMetadata["profile_photo"] = base64Data

                val request = UpdateUserRequest(
                    name = uiState.currentUser?.name,
                    surname = uiState.currentUser?.surname,
                    metadata = newMetadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {
                    loadProfile()
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo subir la foto de perfil",)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al subir foto de perfil",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }


    fun updateProfile(name: String, surname: String, metadata: Map<String, String>?) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)
            try {
                val request = UpdateUserRequest(
                    name = name,
                    surname = surname,
                    metadata = metadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {
                    loadProfile()
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo actualizar perfil",)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al actualizar perfil",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true,)
            try {
                val response = authRepository.changePassword(currentPassword, newPassword)
                if (response.isSuccessful) {
                    uiState = uiState.copy(passwordChanged = true,)
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo cambiar la contraseña",)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión",)
            } finally {
                uiState = uiState.copy()
            }
        }
    }


    fun clearPasswordChangeState() {
        uiState = uiState.copy(passwordChanged = false, errorMessage = null)
    }
    fun clearErrorMessage() {
        uiState = uiState.copy(errorMessage = null)
    }

}
