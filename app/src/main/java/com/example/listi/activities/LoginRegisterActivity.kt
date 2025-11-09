package com.example.listi.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.listi.AuthRepository
import com.example.listi.ui.screens.LoginScreen
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

                    if (loginSuccess) {
                        // Podrías navegar a otra pantalla o mostrar un mensaje de bienvenida
                        Text("✅ Sesión iniciada con éxito", color = MaterialTheme.colorScheme.primary)
                    } else {
                        LoginScreen(
                            onLoginClick = { email, password ->
                                // Lógica del botón Login
                                lifecycleScope.launch {
                                    isLoading = true
                                    errorMessage = null

                                    try {
                                        val success = authRepository.login(email, password)
                                        if (success) {
                                            loginSuccess = true
                                            println("✅ Login exitoso")
                                            val savedToken = authRepository.getSavedToken()
                                            println("🔐 Token guardado: $savedToken")
                                        } else {
                                            errorMessage = "Credenciales inválidas o error en el servidor"
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
                                println("🧾 Crear cuenta nueva presionado")
                            },
                            onForgotPasswordClick = {
                                println("🔑 Olvidé mi contraseña presionado")
                            }
                        )

                        if (isLoading) {
                            CircularProgressIndicator()
                        }

                        errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
