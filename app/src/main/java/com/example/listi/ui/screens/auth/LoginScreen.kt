package com.example.listi.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.R
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginClick: ((String, String) -> Unit)? = null,
    onCreateAccountClick: (() -> Unit)? = null,
    onVerifyAccount: (() -> Unit)? = null,
    onForgotPasswordClick: (() -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    val configuration = LocalConfiguration.current
    val isLoading by authViewModel.isLoading.collectAsState()

    val errorMessage by authViewModel.errorMessage.collectAsState()
    val connectionError = stringResource(R.string.error_connection)
    val credentialsError = stringResource(R.string.error_credentials_invalid)
    val codeError = stringResource(R.string.error_could_not_resend)
    val mailError = stringResource(R.string.error_mail)
    val verificationError = stringResource(R.string.account_not_verified)

    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            val message = when (error) {
                "1" -> credentialsError
                "400" -> mailError
                "401" -> verificationError
                "2" -> codeError
                else -> connectionError
            }

            snackbarHostState.showSnackbar(message)
            authViewModel.clearError()
        }
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

        else -> {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { padding ->

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                ) {
                    val isLandscape =
                        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                    val screenWidth = configuration.screenWidthDp.dp
                    val circleSize = screenWidth * 1.6f
                    val logoSize = (screenWidth * 0.35f).coerceIn(96.dp, 140.dp)
                    val topContentPadding = if (!isLandscape) circleSize * 0.25f else 0.dp


                    if (!isLandscape) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(circleSize * 0.75f)
                                .align(Alignment.TopCenter),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(circleSize)
                                    .offset(y = -circleSize * 0.4f)
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiaryFixed,
                                        shape = CircleShape
                                    )
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = circleSize * 0.05f)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(logoSize)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    color = MaterialTheme.colorScheme.tertiaryFixed,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    val horizontalPadding = if (isLandscape) 12.dp else 32.dp
                    val verticalPadding = if (isLandscape) 8.dp else 28.dp
                    val spacerSmall = if (isLandscape) 6.dp else 16.dp
                    val spacerMedium = if (isLandscape) 8.dp else 24.dp
                    val buttonHeight = if (isLandscape) 40.dp else 50.dp
                    val titleFontSize = if (isLandscape) 22.sp else 28.sp
                    val iconSize = if (isLandscape) 20.dp else 24.dp

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = horizontalPadding,
                                end = horizontalPadding,
                                top = verticalPadding + topContentPadding,
                                bottom = verticalPadding
                            )
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (isLandscape) Arrangement.SpaceEvenly else Arrangement.Center
                    ) {
                        if (isLandscape) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryFixed,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(bottom = spacerSmall)
                            ) {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontSize = titleFontSize,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(spacerMedium))
                        } else {

                            Spacer(modifier = Modifier.height(spacerMedium))
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(stringResource(R.string.email_label)) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.mail_foreground),
                                    contentDescription = stringResource(R.string.email_label),
                                    modifier = Modifier.size(iconSize)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(spacerSmall))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(stringResource(R.string.password)) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.lock_foreground),
                                    modifier = Modifier.size(iconSize),
                                    contentDescription = stringResource(R.string.password)
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                                focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(spacerMedium))

                        val isLoginEnabled = email.isNotBlank() && password.isNotBlank()

                        Button(
                            onClick = {
                                if (isLoginEnabled) {
                                    onLoginClick?.invoke(email, password)
                                        ?: authViewModel.login(email, password)
                                }
                            },
                            enabled = isLoginEnabled,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLoginEnabled) MaterialTheme.colorScheme.tertiaryFixed else MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = stringResource(R.string.login),
                                color = if (isLoginEnabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                                fontSize = if (isLandscape) 16.sp else 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        TextButton(onClick = { onForgotPasswordClick?.invoke() }) {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                fontSize = if (isLandscape) 12.sp else 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        TextButton(onClick = { onVerifyAccount?.invoke() }) {
                            Text(
                                text = stringResource(R.string.verify),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(spacerSmall))

                        OutlinedButton(
                            onClick = { onCreateAccountClick?.invoke() },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = stringResource(R.string.registration),
                                fontSize = if (isLandscape) 14.sp else 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    //LoginScreen()
}
