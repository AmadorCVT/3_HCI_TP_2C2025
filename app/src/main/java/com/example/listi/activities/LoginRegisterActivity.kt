package com.example.listi.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.screens.LoginScreen
import com.example.listi.ui.screens.RegisterScreen
import com.example.listi.ui.theme.ListiTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepository = AuthRepository(this)

        setContent {
            ListiTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    var isLoading by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }
                    var loginSuccess by remember { mutableStateOf(false) }
                    var showRegisterScreen by remember { mutableStateOf(false) }

                    when {
                        loginSuccess -> {
                            Text(
                                text = "✅ Sesión iniciada con éxito",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        showRegisterScreen -> {
                            RegisterScreen(
                                onRegisterClick = { firstName, lastName, email, password ->
                                    lifecycleScope.launch {
                                        isLoading = true
                                        errorMessage = null

                                        try {
                                            val success = authRepository.register(
                                                firstName = firstName,
                                                lastName = lastName,
                                                email = email,
                                                password = password
                                            )
                                            if (success) {
                                                showRegisterScreen = false
                                                println("✅ Registro exitoso")
                                            } else {
                                                errorMessage = "Error al registrar usuario"
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            errorMessage = "Error: ${e.localizedMessage}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                },
                                onGoLoginClick = {
                                    showRegisterScreen = false
                                },
                                onVerifyClick = {
                                    println("🔍 Verificar usuario presionado")
                                }
                            )
                        }

                        else -> {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    lifecycleScope.launch {
                                        isLoading = true
                                        errorMessage = null

                                        try {
                                            val success = authRepository.login(email, password)
                                            if (success) {
                                                loginSuccess = true
                                                val savedToken = authRepository.getSavedToken()
                                                println("✅ Login exitoso. Token: $savedToken")
                                            } else {
                                                errorMessage = "Credenciales inválidas o error del servidor"
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            errorMessage = "Error: ${e.localizedMessage}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                },
                                onCreateAccountClick = {
                                    showRegisterScreen = true
                                },
                                onForgotPasswordClick = {
                                    println("🔑 Olvidé mi contraseña presionado")
                                }
                            )
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
