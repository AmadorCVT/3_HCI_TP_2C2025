package com.example.listi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.R
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.LightGreen
import kotlinx.coroutines.launch

@Composable
fun RecoverPasswordScreenCode(
    authViewModel: AuthViewModel,
    goRestorePassword: (() -> Unit)? = null
) {
    val uiState = authViewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }


    val invalidMailMessage = stringResource(R.string.error_mail)
    val instructionsSentMessage = stringResource(R.string.sent_instruction)
    val fillFieldsMessage = stringResource(R.string.fill_fields)

    LaunchedEffect(uiState.forgotCodeSent) {
        if (uiState.forgotCodeSent) {
            snackbarHostState.showSnackbar(instructionsSentMessage)
            goRestorePassword?.invoke()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        // Fondo verde pastel como en la imagen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F8D6)) // mismo tono pastel claro
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = stringResource(R.string.password_recovery),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo
            Text(
                text = stringResource(R.string.password_recovery_steps),
                style = MaterialTheme.typography.bodyLarge,
                color = DarkGrey,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.mail_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = DarkGreen,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Botón enviar
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        authViewModel.sendForgotPasswordCode(email)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(invalidMailMessage)
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
                    text = stringResource(R.string.send_instructions),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
