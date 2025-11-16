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

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }
    private val _isLogged = MutableStateFlow(false)
    val isLogged = _isLogged.asStateFlow()

    private val _isVerified = MutableStateFlow(false)
    val isVerified = _isVerified.asStateFlow()

    private val _passwordChanged = MutableStateFlow(false)
    val passwordChanged = _passwordChanged.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _forgotCodeSent = MutableStateFlow(false)
    val forgotCodeSent = _forgotCodeSent.asStateFlow()

    private val _showVerification = MutableStateFlow(false)
    val showVerification = _showVerification.asStateFlow()

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

                _isLogged.value = true
                _token.value = token

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

            _isLoading.value = true
            try {

                val response = authRepository.register(firstName, lastName, email, password)

                if (response.isSuccessful) {
                    _showVerification.value = true

                } else {
                    _errorMessage.value = "400"

                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _showVerification.value = false
                _isLoading.value = false
            }
        }
    }


    fun verifyAccount(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.verifyAccount(code)
                if (response.isSuccessful) {
                    _isVerified.value = true
                } else {
                    _errorMessage.value = "Código incorrecto"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun resendVerificationCode(email: String) {
        viewModelScope.launch {
            try {
                authRepository.sendVerificationCode(email)
                _forgotCodeSent.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }


    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.resetPassword(code, newPassword)
                if (response.isSuccessful) {
                    _passwordChanged.value = true
                } else {
                    _errorMessage.value = "No se pudo restablecer la contraseña"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendForgotPasswordCode(email: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = authRepository.forgotPassword(email)

                if (response.isSuccessful) {
                    _forgotCodeSent.value = true
                } else {
                    _errorMessage.value = "2"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {

            } finally {
                // Resetear todo
                _isLogged.value = false
                _isVerified.value = false
                _passwordChanged.value = false
                _token.value = null
                 _currentUser.value = null
                _forgotCodeSent.value = false
                 _showVerification.value = false
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
                    _currentUser.value = user
                    _isLogged.value = true
                } else {
                    _errorMessage.value = "No se pudo obtener perfil"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            }
        }
    }

    fun updateProfileMetadata(metadata: Map<String, String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val current = _currentUser.value
                val request = UpdateUserRequest(
                    name = current?.name,
                    surname = current?.surname,
                    metadata = metadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {

                    loadProfile()
                } else {
                    _errorMessage.value = "No se pudo actualizar perfil"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateProfilePhoto(base64Data: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val existing = _currentUser.value?.metadata ?: emptyMap()
                val newMetadata = existing.toMutableMap()
                newMetadata["profile_photo"] = base64Data

                val request = UpdateUserRequest(
                    name = _currentUser.value?.name,
                    surname = _currentUser.value?.surname,
                    metadata = newMetadata
                )
                val response = authRepository.updateProfile(request)
                if (response.isSuccessful) {
                    loadProfile()
                } else {
                    _errorMessage.value = "No se pudo subir la foto de perfil"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun updateProfile(name: String, surname: String, metadata: Map<String, String>?) {
        viewModelScope.launch {
            _isLoading.value = true
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
                    _errorMessage.value = "No se pudo actualizar perfil"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.changePassword(currentPassword, newPassword)
                if (response.isSuccessful) {
                    _passwordChanged.value = true
                } else {
                    _errorMessage.value = "No se pudo cambiar la contraseña"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clearPasswordChangeState() {
        _passwordChanged.value = false
        _errorMessage.value = null
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

}
