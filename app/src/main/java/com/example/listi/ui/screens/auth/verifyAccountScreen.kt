package com.example.listi.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.ListiGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.DarkGray
import kotlinx.coroutines.launch

@Composable
fun VerifyAccountScreen(
    authViewModel: AuthViewModel,
    goLogin: (() -> Unit)? = null,
) {
    val uiState = authViewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var code by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(uiState.currentUser?.email ?: "") }

    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) {
            scope.launch { snackbarHostState.showSnackbar("Cuenta verificada correctamente") }
            goLogin?.invoke()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            authViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Text(
                text = "Verificar cuenta",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = DarkGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Ingresá tu correo y enviá el código de verificación.",
                style = MaterialTheme.typography.bodyLarge,
                color = DarkGrey,
                modifier = Modifier.padding(bottom = 32.dp)
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = DarkGreen)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        authViewModel.resendVerificationCode(email)
                        scope.launch {
                            snackbarHostState.showSnackbar("Código enviado a $email")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Ingresá un email válido")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ListiGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("ENVIAR CÓDIGO")
            }

            Spacer(Modifier.height(24.dp))

            
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = { Text("Código de verificación") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = DarkGreen
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))


            val isEnabled = code.isNotBlank()

            Button(
                onClick = { authViewModel.verifyAccount(code) },
                enabled = isEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEnabled) ListiGreen else DarkGrey,
                    contentColor = if (isEnabled) Color.White else DarkGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "VERIFICAR CUENTA",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
