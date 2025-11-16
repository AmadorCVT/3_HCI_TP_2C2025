package com.example.listi.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.ListiGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.DarkGray
import com.example.listi.ui.theme.White
import com.example.listi.R
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(authViewModel: AuthViewModel,
                   goLogin: (() -> Unit)? = null,
                   goVerifyAccount: (() -> Unit)? = null) {

    val viewModel = authViewModel

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }


    LaunchedEffect(uiState.showVerification) {
        if (uiState.showVerification) {
            if (goLogin != null) {
                goLogin()
            }
        }
    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
            }

            authViewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Listi",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.register),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.name)) },
            singleLine = true,
            leadingIcon = { Icon(
                imageVector = ImageVector.vectorResource(R.drawable.person_foreground),
                contentDescription = "Nombre",
                modifier = Modifier.size(24.dp)
            ) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.profile_surname_label)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    ImageVector.vectorResource(R.drawable.badge),
                    contentDescription = "Apellido"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            leadingIcon = { Icon(
                imageVector = ImageVector.vectorResource(R.drawable.mail_foreground),
                contentDescription = "Email",
                modifier = Modifier.size(24.dp)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            leadingIcon = { Icon(
                imageVector = ImageVector.vectorResource(R.drawable.lock_foreground),
                contentDescription = "Con",
                modifier = Modifier.size(24.dp)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text(stringResource(R.string.profile_confirm_password)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    ImageVector.vectorResource(R.drawable.lock_reset),
                    contentDescription = "Repetir contraseña"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        val isCreateEnabled = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && (password == repeatPassword)

        Button(
            onClick = {
                if (isCreateEnabled) {
                    viewModel.register(firstName, lastName, email, password)
                }
            },
            enabled = isCreateEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCreateEnabled) ListiGreen else DarkGrey,
                contentColor = if (isCreateEnabled) White else DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = if (isCreateEnabled) White else DarkGray,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.signin),
                    color = if (isCreateEnabled) White else DarkGray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        TextButton(onClick = { goLogin?.invoke() }) {
            Text(
                text = stringResource(R.string.got_an_acount),
                color = DarkGreen,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { goVerifyAccount?.invoke() }) {
            Text(
                text = stringResource(R.string.verify),
                color = DarkGreen,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
