package com.example.listi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.listi.ui.theme.ListiGreen

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryGreen,
    primaryContainer = DarkPrimaryContainer,

    secondary = DarkAccentGreen,
    tertiary = DarkAccentGreen,

    background = DarkBackground,
    surface = DarkPrimaryContainer,
    surfaceVariant = DarkAccentGreen,

    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,

    onBackground = White,
    onSurface =DarkGrey ,
    tertiaryFixed = ListiGreen,
    error = Color.Red
)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,
    primaryContainer = LightGreen ,
    secondary = DarkGrey,
    surfaceVariant = DarkGreen,
    tertiary = Green,
    background = backColor,
    surface = White,
    tertiaryFixed = ListiGreen,
    onPrimary = Green,
    onSecondary = White,
    onTertiary = White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = White,
    error = Color.Red
)

@Composable
fun ListiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}