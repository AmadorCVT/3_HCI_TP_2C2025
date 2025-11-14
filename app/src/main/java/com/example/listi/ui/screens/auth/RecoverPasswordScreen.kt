package com.example.listi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.LightGreen
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password


@Composable
fun ResetPasswordScreen(
    authViewModel: AuthViewModel,
    goLogin: (() -> Unit)? = null
) {
    val uiState = authViewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    // Cuando la contraseña fue cambiada exitosamente → volver al login
    LaunchedEffect(uiState.passwordChanged) {
        if (uiState.passwordChanged) {
            snackbarHostState.showSnackbar("Contraseña actualizada correctamente")
            goLogin?.invoke()
        }
    }

    // Mostrar errores
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F8D6))
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Restablecer contraseña",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ingresá el código recibido y tu nueva contraseña.",
                fontSize = 14.sp,
                color = DarkGrey,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Código
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = { Text("Código de recuperación") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = DarkGreen)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LightGreen,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nueva contraseña
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("Nueva contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Password, contentDescription = null, tint = DarkGreen)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LightGreen,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = DarkGreen
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (code.isNotBlank() && newPassword.isNotBlank()) {
                        authViewModel.resetPassword(code, newPassword)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Completá todos los campos")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = DarkGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "CAMBIAR CONTRASEÑA",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
