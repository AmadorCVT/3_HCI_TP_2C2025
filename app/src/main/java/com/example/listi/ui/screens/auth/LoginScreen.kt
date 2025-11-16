package com.example.listi.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.R
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.LightGreen
import com.example.listi.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginClick: ((String, String) -> Unit)? = null,
    onCreateAccountClick: (() -> Unit)? = null,
    onForgotPasswordClick: (() -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState = authViewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }

            authViewModel.clearErrorMessage()
        }
    }
    // Use a Box as root so the circle+logo can be drawn as an overlay (no layout space consumed)
    Box(modifier = Modifier.fillMaxSize()) {
        // Overlay: large green circle and logo positioned at top center and offset so they protrude from top
        Box(modifier = Modifier
            .align(Alignment.TopCenter)
            .wrapContentSize(), contentAlignment = Alignment.TopCenter) {
            // Circle (kept as you had it, big and moved upwards)
            Box(
                modifier = Modifier
                    .size(520.dp)
                    .offset(y = (-300).dp)
                    .background(
                        color = colorResource(id = R.color.listi_green),
                        shape = CircleShape
                    )
            )

            // Logo drawn on top and shifted to sit into the green area
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(140.dp)
                    .offset(y = 18.dp)
            )
        }

        // Main content: form and buttons — stays centered and is not pushed down by the overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = stringResource(R.string.email_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = stringResource(R.string.password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLoginClick?.invoke(email, password) ?: authViewModel.login(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { onForgotPasswordClick?.invoke() }) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = DarkGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onCreateAccountClick?.invoke() },
                border = BorderStroke(1.dp, DarkGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen)
            ) {
                Text(
                    text = stringResource(R.string.registration),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    //LoginScreen()
}
