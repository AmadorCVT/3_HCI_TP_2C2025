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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.R
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isVerified by authViewModel.isVerified.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var code by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }

    val verifySuccessMessage = stringResource(R.string.verification_sucess)

    LaunchedEffect(isVerified) {
        if (isVerified) {
            scope.launch { snackbarHostState.showSnackbar(verifySuccessMessage) }
            goLogin?.invoke()
        }
    }


    LaunchedEffect(errorMessage) {
        errorMessage?.let {
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
                text = stringResource(R.string.verify_account),
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = DarkGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.email_for_verification),
                fontSize = 15.sp,
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

            val verificationCodeSentMessage = stringResource(R.string.verification_code_sent)
            val enterValidEmailMessage = stringResource(R.string.enter_valid_email)

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        authViewModel.resendVerificationCode(email)
                        scope.launch {
                            snackbarHostState.showSnackbar("$verificationCodeSentMessage $email")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(enterValidEmailMessage)
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
                Text(stringResource(R.string.send_code))
            }

            Spacer(Modifier.height(24.dp))


            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = { Text(stringResource(R.string.profile_verification_code)) },
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
                    containerColor = ListiGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
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



