package com.example.listi.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.example.listi.ui.screens.LoginScreen
import com.example.listi.ui.theme.ListiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListiTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginScreen(
                        onLoginClick = {

                            println("Botón 'Iniciar sesión' presionado")

                        },
                        onCreateAccountClick = {
                            println("Botón 'Crear cuenta nueva' presionado")

                        },
                        onForgotPasswordClick = {
                            println("Botón 'Olvidaste tu contraseña' presionado")

                        }
                    )
                }
            }
        }
    }
}