package com.example.listi.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.types.User
import com.example.listi.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel? = null,
    onEditClick: () -> Unit = {},
    onChangePhoto: () -> Unit = {}
) {
    // obtener recursos localizados una sola vez en el contexto composable
    val spanishLabel = stringResource(R.string.language_spanish)
    val englishLabel = stringResource(R.string.language_english)
    val profileLanguageLabel = stringResource(R.string.profile_language_label)

    var selectedLanguage by remember { mutableStateOf(spanishLabel) }

    // Cargar perfil si no está cargado
    LaunchedEffect(Unit) {
        if (authViewModel != null && authViewModel.uiState.currentUser == null) {
            authViewModel.loadProfile()
        }
    }

    val user: User? = authViewModel?.uiState?.currentUser

    val name = user?.let { "${it.name} ${it.surname}" } ?: ""
    val email = user?.email ?: ""
    // metadata puede contener sexo y birthDate; como no hay clave definida, usamos placeholders
    val sex = user?.metadata?.get("sex") ?: stringResource(R.string.not_specified)
    val birthDate = user?.metadata?.get("birthDate") ?: stringResource(R.string.profile_placeholder_dash)
    val initials = user?.let {
        val n = it.name.trim()
        val s = it.surname.trim()
        "${n.firstOrNull()?.uppercaseChar() ?: 'A'}${s.firstOrNull()?.uppercaseChar() ?: 'C'}"
    } ?: "AC"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            // Botón fijo en el fondo de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.profile_edit_button))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .padding(innerPadding),
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
                    Text(
                        text = stringResource(R.string.profile_change_photo),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onChangePhoto() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre
                    ProfileField(label = stringResource(R.string.profile_name_label), value = if (name.isBlank()) stringResource(R.string.profile_placeholder_dash) else name)
                    Spacer(modifier = Modifier.height(30.dp))

                    // Gmail
                    ProfileField(label = stringResource(R.string.profile_email_label), value = if (email.isBlank()) stringResource(R.string.profile_placeholder_dash) else email)
                    Spacer(modifier = Modifier.height(30.dp))

                    // Sexo
                    ProfileField(label = stringResource(R.string.profile_sex_label), value = sex)
                    Spacer(modifier = Modifier.height(30.dp))

                    // Fecha de nacimiento
                    ProfileField(label = stringResource(R.string.profile_birthdate_label), value = birthDate)
                    Spacer(modifier = Modifier.height(30.dp))

                    // Idioma
                    LanguageSelectorField(
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { onLanguageChange -> selectedLanguage = onLanguageChange },
                        spanishLabel = spanishLabel,
                        englishLabel = englishLabel,
                        profileLanguageLabel = profileLanguageLabel
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.height(80.dp)) // espacio extra para que el contenido no quede oculto detrás del bottomBar
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
    if (isSelected) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = text, fontSize = 12.sp)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = text, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
