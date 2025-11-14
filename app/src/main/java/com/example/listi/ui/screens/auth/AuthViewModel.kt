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
                    // cargar perfil después del login
                    loadProfile()
                } else {
                    uiState = uiState.copy(errorMessage = "Credenciales inválidas")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed", e)
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

    // --------------------------
    // PROFILE
    // --------------------------
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
                    uiState = uiState.copy(currentUser = user)
                } else {
                    // opcional: setear errorMessage
                    uiState = uiState.copy(errorMessage = "No se pudo obtener perfil")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error de conexión al obtener perfil")
            }
        }
    }

    /**
     * Actualiza la metadata del perfil (mapa parcial). En caso de exito recarga el perfil.
     */
    fun updateProfileMetadata(metadata: Map<String, String>) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val current = uiState.currentUser
                val request = UpdateUserRequest(
                    name = current?.name,
                    surname = current?.surname,
                    metadata = metadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {
                    // recargar perfil para reflejar cambios
                    loadProfile()
                } else {
                    uiState = uiState.copy(errorMessage = "No se pudo actualizar perfil")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al actualizar perfil")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    /**
     * Sube la foto codificada en base64 y guarda en metadata con la clave "profile_photo".
     */
    fun updateProfilePhoto(base64Data: String) {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, errorMessage = null)
                // construir metadata existente + nueva entrada
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
                    uiState = uiState.copy(errorMessage = "No se pudo subir la foto de perfil")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al subir foto de perfil")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    /**
     * Actualiza el perfil con nombre, apellido y metadata proporcionados.
     */
    fun updateProfile(name: String, surname: String, metadata: Map<String, String>?) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
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
                    uiState = uiState.copy(errorMessage = "No se pudo actualizar perfil")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message ?: "Error al actualizar perfil")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
}
