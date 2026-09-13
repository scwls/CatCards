package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimary,
    onPrimary = AppOnLight,
    primaryContainer = AppPrimaryContainer,
    onPrimaryContainer = AppOnLight,
    secondary = AppSecondary,
    onSecondary = AppOnLight,
    tertiary = AppTertiary,
    onTertiary = AppOnLight,
    background = AppBackground,
    onBackground = AppOnDark,
    surface = AppSurface,
    onSurface = AppOnDark,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = AppOnDark,
    error = AppError,
    onError = AppOnLight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
