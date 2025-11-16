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
import com.example.listi.ui.theme.DarkGreen
import com.example.listi.ui.theme.ListiGreen
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.DarkGray
import com.example.listi.ui.theme.White
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import com.example.listi.ui.theme.LightGreen

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

    val configuration = LocalConfiguration.current

    val isLoading by authViewModel.isLoading.collectAsState()

    val errorMessage by authViewModel.errorMessage.collectAsState()
    val connectionError = stringResource(R.string.error_connection)
    val credentialsError = stringResource(R.string.error_credentials_invalid)
    val codeError = stringResource(R.string.error_could_not_resend)


    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            val message = when (error) {
                "1" -> credentialsError
                "400" -> credentialsError
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
                // Use a Box as root so the circle+logo can be drawn as an overlay (no layout space consumed)
                Box(modifier = Modifier.fillMaxSize()) {
                    // Overlay: large green circle and logo positioned at top center and offset so they protrude from top
                    // Mostrar el overlay sólo cuando NO estamos en orientación horizontal
                    if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .wrapContentSize(), contentAlignment = Alignment.TopCenter
                        ) {
                            // Circle (kept as you had it, big and moved upwards)
                            Box(
                                modifier = Modifier
                                    .size(520.dp)
                                    .offset(y = (-300).dp)
                                    .background(
                                        color = ListiGreen,
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
                    }

                    // Main content: form and buttons — stays centered and is not pushed down by the overlay
                    // Ajustes para orientar en horizontal: menos padding, espaciados más pequeños y botones más cortos
                    val isLandscape =
                        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLandscape) Arrangement.SpaceEvenly else Arrangement.Center
        ) {
            Surface(
                color = ListiGreen,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = spacerSmall)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = White,
                    fontSize = titleFontSize,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

                        if (!isLandscape) {
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
                            modifier = Modifier.fillMaxWidth()
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
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(spacerMedium))


                        val isLoginEnabled = email.isNotBlank() && password.isNotBlank()

            Button(
                onClick = { if (isLoginEnabled) (onLoginClick?.invoke(email, password) ?: authViewModel.login(email, password)) },
                enabled = isLoginEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLoginEnabled) ListiGreen else LightGreen,
                    contentColor = White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = if (isLoginEnabled) White else DarkGray,
                    fontSize = if (isLandscape) 16.sp else 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }


                        TextButton(onClick = { onForgotPasswordClick?.invoke() }) {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = DarkGreen,
                                fontSize = if (isLandscape) 12.sp else 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(spacerSmall))

                        OutlinedButton(
                            onClick = { onCreateAccountClick?.invoke() },
                            border = BorderStroke(1.dp, DarkGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen)
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
