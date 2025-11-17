package com.example.listi.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.listi.R
@Composable
fun RegisterScreen(authViewModel: AuthViewModel,
                   goLogin: (() -> Unit)? = null,
                   goVerifyAccount: (() -> Unit)? = null) {

    val viewModel = authViewModel

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val showVerification by authViewModel.showVerification.collectAsState()

    LaunchedEffect(showVerification) {
        if (showVerification) {
            if (goLogin != null) {
                goLogin()
            }
        }
    }

    val configuration = LocalConfiguration.current

    val isLoading by authViewModel.isLoading.collectAsState()

    val errorMessage by authViewModel.errorMessage.collectAsState()
    val connectionError = stringResource(R.string.error_connection)
    val credentialsError = stringResource(R.string.error_credentials_invalid)
    val codeError = stringResource(R.string.error_could_not_resend)
    val mailError = stringResource(R.string.mail_already_registered)

    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            val message = when (error) {
                "1" -> credentialsError
                "400" -> credentialsError
                "401" -> credentialsError
                "409" -> mailError
                "2" -> codeError
                else -> connectionError
            }

            snackbarHostState.showSnackbar(message)

            authViewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.person_foreground),
                            contentDescription = "Nombre",
                            modifier = Modifier.size(24.dp)
                        )
                    },
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
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.mail_foreground),
                            contentDescription = "Email",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.lock_foreground),
                            contentDescription = "Con",
                            modifier = Modifier.size(24.dp)
                        )
                    },
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

                // Notificar que las que son invalidas
                if(password != repeatPassword)
                    Text(
                        text = stringResource(R.string.same_password),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                else if(password.length < 6)
                    Text(
                        text = stringResource(R.string.password_length),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                val isCreateEnabled =
                    firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && (password == repeatPassword)

                Button(
                    onClick = {
                        if (isCreateEnabled) {
                            viewModel.register(firstName, lastName, email, password)
                        }
                    },
                    enabled = isCreateEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCreateEnabled) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primary,
                        contentColor = if (isCreateEnabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (isLoading) {
                        Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) { CircularProgressIndicator() }
                    } else {
                        Text(
                            text = stringResource(R.string.signin),
                            color = if (isCreateEnabled) White else MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(onClick = { goLogin?.invoke() }) {
                    Text(
                        text = stringResource(R.string.got_an_acount),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { goVerifyAccount?.invoke() }) {
                    Text(
                        text = stringResource(R.string.verify),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
