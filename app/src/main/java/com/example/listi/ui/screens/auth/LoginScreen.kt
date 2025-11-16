package com.example.listi.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.listi.ui.theme.LightGreen
import com.example.listi.ui.theme.White
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

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
        // Mostrar el overlay sólo cuando NO estamos en orientación horizontal
        if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
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
        }

        // Main content: form and buttons — stays centered and is not pushed down by the overlay
        // Ajustes para orientar en horizontal: menos padding, espaciados más pequeños y botones más cortos
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = if (configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) colorResource(id = R.color.white) else colorResource(id = R.color.listi_green),
                fontSize = titleFontSize,
                modifier = Modifier.padding(bottom = spacerMedium)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label)) },
                singleLine = true,
                leadingIcon = { Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.mail_foreground),
                    contentDescription = stringResource(R.string.email_label),
                    modifier = Modifier.size(iconSize)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacerSmall))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                singleLine = true,
                leadingIcon = { Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.lock_foreground),
                    modifier = Modifier.size(iconSize),
                    contentDescription = stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacerMedium))

            Button(
                onClick = { onLoginClick?.invoke(email, password) ?: authViewModel.login(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = White,
                    fontSize = if (isLandscape) 16.sp else 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(spacerSmall))

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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    //LoginScreen()
}
