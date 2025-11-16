package com.example.listi.ui.screens.auth

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.OpenableColumns
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.types.User
import com.example.listi.R
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.listi.ui.theme.ListiGreen

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    // obtener recursos localizados una sola vez en el contexto composable
    val spanishLabel = stringResource(R.string.language_spanish)
    val englishLabel = stringResource(R.string.language_english)
    val profileLanguageLabel = stringResource(R.string.profile_language_label)

    var selectedLanguage by remember { mutableStateOf(spanishLabel) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    val user by authViewModel.currentUser.collectAsState()

    var isEditing by remember { mutableStateOf(false) }


    var editName by remember { mutableStateOf(user?.name ?: "") }
    var editSurname by remember { mutableStateOf(user?.surname ?: "") }

    var pendingPhotoBase64 by remember { mutableStateOf<String?>(null) }

     var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var changeError by remember { mutableStateOf<String?>(null) }

    val changeErrorEmptyStr = stringResource(R.string.profile_change_password_error_empty)
    val changeErrorMismatchStr = stringResource(R.string.profile_change_password_error_mismatch)
    val changeErrorTooShortStr = stringResource(R.string.profile_change_password_error_too_short)

   val changePasswordSuccessMsg = stringResource(R.string.change_password_success)
    val changePasswordFailureMsg = stringResource(R.string.change_password_failure)

     var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var passwordOperationCompleted by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val name = user?.let { "${it.name} ${it.surname}" } ?: ""
    val email = user?.email ?: ""
    val initials = user?.let {
        val n = it.name.trim()
        val s = it.surname.trim()
        "${n.firstOrNull()?.uppercaseChar() ?: ' '}${s.firstOrNull()?.uppercaseChar() ?: ' '}"
    } ?: " "

    val context = LocalContext.current

    // máximo permitido 2 MB
    val MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024L
    var showImageTooLargeDialog by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try {

                val cursor = context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                        val size = if (sizeIndex != -1) it.getLong(sizeIndex) else -1L
                        if (size > MAX_IMAGE_SIZE_BYTES) {
                            showImageTooLargeDialog = true
                            return@rememberLauncherForActivityResult
                        }
                    }
                }

                val input = context.contentResolver.openInputStream(uri)
                val options = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
                val originalBitmap = BitmapFactory.decodeStream(input, null, options)
                input?.close()

                if (originalBitmap != null) {
                    // Redimensionar si es necesario (max 1024px)
                    val maxDim = 1024
                    val width = originalBitmap.width
                    val height = originalBitmap.height
                    val scale = if (width > height) {
                        if (width > maxDim) maxDim.toFloat() / width else 1f
                    } else {
                        if (height > maxDim) maxDim.toFloat() / height else 1f
                    }

                    val bitmapToCompress: Bitmap = if (scale < 1f) {
                        val newW = (width * scale).toInt()
                        val newH = (height * scale).toInt()
                        originalBitmap.scale(newW, newH, true)
                    } else {
                        originalBitmap
                    }

                    val baos = ByteArrayOutputStream()
                    bitmapToCompress.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                    val bytes = baos.toByteArray()
                    baos.close()

                    val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                    pendingPhotoBase64 = base64
                }
            } catch (_: Exception) {
            }
        }
    }

    val profilePhotoBitmap = remember(user?.metadata, pendingPhotoBase64) {
        val src = pendingPhotoBase64 ?: user?.metadata?.get("profile_photo")
        if (src.isNullOrBlank()) null
        else try {
            val bytes = Base64.decode(src, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (_: Exception) {
            null
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
            var contentHeightPx by remember { mutableStateOf(0f) }
            val needsScroll = contentHeightPx > maxHeightPx
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .then(if (needsScroll) Modifier.verticalScroll(scrollState) else Modifier)
                    .padding(8.dp)
                    .padding(innerPadding)
                    .onGloballyPositioned { coords ->
                        contentHeightPx = coords.size.height.toFloat()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Card con colores del tema
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top-left: botón para cambiar contraseña
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp)
                        ) {
                            // botón en la esquina superior izquierda
                            Box(modifier = Modifier.align(Alignment.TopStart)) {
                                ElevatedButton(
                                    onClick = { showChangePasswordDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(text = stringResource(R.string.profile_change_password),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            if (!isEditing) {
                                 ElevatedButton(
                                    onClick = { isEditing = true },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.edit_foreground),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = stringResource(R.string.edit),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                            } else {
                                 ElevatedButton(
                                    onClick = {
                                         isEditing = false
                                        pendingPhotoBase64 = null
                                        editName = user?.name ?: ""
                                        editSurname = user?.surname ?: ""
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.close_foreground),
                                        contentDescription = stringResource(R.string.cancel),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .border(width = 3.dp, color = ListiGreen, shape = CircleShape)
                                .clickable(enabled = isEditing) { if (isEditing) pickImageLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (profilePhotoBitmap != null) {
                                Image(
                                    bitmap = profilePhotoBitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Text(
                                    text = initials,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Indicador pequeño para cambiar la foto cuando se está editando
                        if (isEditing) {
                            Text(
                                text = stringResource(R.string.profile_change_photo),
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clickable { pickImageLauncher.launch("image/*") },
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Editable name and surname
                        if (isEditing) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text(stringResource(R.string.profile_name_label)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = editSurname,
                                onValueChange = { editSurname = it },
                                label = { Text(stringResource(R.string.profile_surname_label)) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Save / Cancel
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ElevatedButton(onClick = {
                                    // Save changes
                                    // Construir metadata nueva fusionando existente y foto pendiente
                                    val existing = user?.metadata?.toMutableMap() ?: mutableMapOf()
                                    if (pendingPhotoBase64 != null) {
                                        existing["profile_photo"] = pendingPhotoBase64!!
                                    }
                                    authViewModel?.updateProfile(editName.trim(), editSurname.trim(), existing)
                                    // limpiar pending
                                    pendingPhotoBase64 = null
                                    isEditing = false
                                }) {
                                    Text(text = stringResource(R.string.save))
                                }

                                TextButton(onClick = {
                                    // Cancel edits, reset fields
                                    isEditing = false
                                    editName = user?.name ?: ""
                                    editSurname = user?.surname ?: ""
                                }) {
                                    Text(text = stringResource(R.string.cancel))
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                        } else {
                            // Display fields read-only
                            ProfileField(label = stringResource(R.string.profile_name_label), value = if (name.isBlank()) stringResource(R.string.profile_placeholder_dash) else name)
                            Spacer(modifier = Modifier.height(10.dp))


                            ProfileField(label = stringResource(R.string.profile_email_label), value = if (email.isBlank()) stringResource(R.string.profile_placeholder_dash) else email)

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { showLogoutDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.logout),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge)
                            }
                           
                        }


                    }
                }

                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = { Text(text = stringResource(R.string.logout_confirm_title)) },
                        text = { Text(text = stringResource(R.string.logout_confirm_message)) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Ejecutar logout si el ViewModel está presente
                                    authViewModel?.logout()
                                    showLogoutDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                )
                            ) {
                                Text(text = stringResource(R.string.logout_confirm_confirm))
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showLogoutDialog = false }) {
                                Text(text = stringResource(R.string.logout_confirm_cancel))
                            }
                        }
                    )
                }

                // Dialogo cuando la imagen es demasiado grande (usa la variable showImageTooLargeDialog)
                if (showImageTooLargeDialog) {
                    AlertDialog(
                        onDismissRequest = { showImageTooLargeDialog = false },
                        title = { Text(text = "Image too large") },
                        text = { Text(text = "The selected image exceeds the 2 MB limit. Please choose a smaller file.") },
                        confirmButton = {
                            Button(onClick = { showImageTooLargeDialog = false }) {
                                Text(text = "OK")
                            }
                        }
                    )
                }

                // Diálogo para cambio de contraseña
                if (showChangePasswordDialog) {
                    AlertDialog(
                        onDismissRequest = { showChangePasswordDialog = false },
                        title = { Text(text = stringResource(R.string.change_password_dialog_title)) },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = currentPassword,
                                    onValueChange = { currentPassword = it },
                                    label = { Text(stringResource(R.string.profile_current_password)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    trailingIcon = {
                                        IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                            val icon = if (showCurrentPassword) ImageVector.vectorResource(R.drawable.visibility_off_foreground) else ImageVector.vectorResource(R.drawable.visibility_foreground)
                                            Icon(imageVector = icon, contentDescription = null)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = newPass,
                                    onValueChange = { newPass = it },
                                    label = { Text(stringResource(R.string.change_password_new_label)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    trailingIcon = {
                                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                            val icon = if (showNewPassword) ImageVector.vectorResource(R.drawable.visibility_off_foreground) else ImageVector.vectorResource(R.drawable.visibility_foreground)
                                            Icon(imageVector = icon, contentDescription = null)
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = confirmPass,
                                    onValueChange = { confirmPass = it },
                                    label = { Text(stringResource(R.string.change_password_confirm_label)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    trailingIcon = {
                                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                            val icon = if (showConfirmPassword) ImageVector.vectorResource(R.drawable.visibility_off_foreground) else ImageVector.vectorResource(R.drawable.visibility_foreground)
                                            Icon(imageVector = icon, contentDescription = null)
                                        }
                                    }
                                )

                                changeError?.let { errorMessage ->
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Validar campos antes de enviar
                                    if (currentPassword.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
                                        changeError = changeErrorEmptyStr
                                    } else if (newPass != confirmPass) {
                                        changeError = changeErrorMismatchStr
                                    } else if (newPass.length < 8) {
                                        changeError = changeErrorTooShortStr
                                    } else {
                                        // Llamar a changePassword (actual -> nueva)
                                        passwordOperationCompleted = true
                                        authViewModel?.changePassword(currentPassword.trim(), newPass.trim())
                                        // no cerrar el diálogo aquí; lo cerramos cuando recibamos la respuesta y mostremos el snackbar
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text(text = stringResource(R.string.change_password_submit))
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = {
                                showChangePasswordDialog = false
                                changeError = null
                            }) {
                                Text(text = stringResource(R.string.change_password_cancel))
                            }
                        }
                    )
                }

                // Mostrar resultado (éxito / error) después de intentar cambiar la contraseña
                val passwordChanged by authViewModel.passwordChanged.collectAsState()
                val passwordErrorMessage by authViewModel.errorMessage.collectAsState()

                if (passwordOperationCompleted && (passwordChanged || passwordErrorMessage != null)) {
                    LaunchedEffect(passwordOperationCompleted, passwordChanged, passwordErrorMessage) {
                        showChangePasswordDialog = false
                        currentPassword = ""
                        newPass = ""
                        confirmPass = ""
                        changeError = null

                        val message = if (passwordChanged) {
                            changePasswordSuccessMsg
                        } else {
                            passwordErrorMessage ?: changePasswordFailureMsg
                        }
                         snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)

                        authViewModel?.clearPasswordChangeState()
                        passwordOperationCompleted = false
                    }
                }

            }
        }
    }
}

@Composable
private fun LanguageSelectorField(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    spanishLabel: String,
    englishLabel: String,
    profileLanguageLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = profileLanguageLabel,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageButton(
                text = spanishLabel,
                isSelected = selectedLanguage == spanishLabel,
                onClick = { onLanguageChange(spanishLabel) }
            )
            LanguageButton(
                text = englishLabel,
                isSelected = selectedLanguage == englishLabel,
                onClick = { onLanguageChange(englishLabel) }
            )
        }
    }
}

@Composable
private fun LanguageButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val colors = ButtonDefaults.buttonColors(
        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    )

    Button(
        onClick = onClick,
        colors = colors,
        shape = shape,
        modifier = Modifier.height(36.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
   //ProfileScreen()
}
