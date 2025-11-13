package com.example.listi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.LightGreen
import com.example.listi.ui.theme.DarkGrey
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
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .wrapContentHeight()
                ) {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Text(
                            text = "Verificar cuenta",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Ingresá tu correo y enviá el código de verificación.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = DarkGrey,
                                fontSize = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))


                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Email") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = DarkGreen)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightGreen,
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = DarkGreen
                            )
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
                                containerColor = LightGreen,
                                contentColor = DarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("ENVIAR CÓDIGO")
                        }

                        Spacer(Modifier.height(28.dp))

                        // ----------------------------------------
                        // ✔️ CAMPO CÓDIGO
                        // ----------------------------------------
                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it },
                            placeholder = { Text("Código de verificación") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DarkGreen
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightGreen,
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = DarkGreen
                            )
                        )

                        Spacer(Modifier.height(24.dp))


                        Button(
                            onClick = {
                                if (code.isNotBlank()) {
                                    authViewModel.verifyAccount(code)
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Ingresá el código enviado.")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightGreen,
                                contentColor = DarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = DarkGreen,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "VERIFICAR CUENTA",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

