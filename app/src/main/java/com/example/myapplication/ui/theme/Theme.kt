package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimary,
    onPrimary = AppOnLight,
    primaryContainer = AppPrimaryContainer,
    onPrimaryContainer = AppOnLight,
    secondary = AppSecondary,
    onSecondary = AppOnLight,
    secondaryContainer = AppSecondaryContainer,
    onSecondaryContainer = AppOnLight,
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

private val PinkColorScheme = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = PinkOnLight,
    primaryContainer = PinkPrimaryContainer,
    onPrimaryContainer = PinkOnDark,
    secondary = PinkSecondary,
    onSecondary = PinkOnLight,
    tertiary = PinkTertiary,
    onTertiary = PinkOnLight,
    background = PinkBackground,
    onBackground = PinkOnDark,
    surface = PinkSurface,
    onSurface = PinkOnDark,
    surfaceVariant = PinkSurfaceVariant,
    onSurfaceVariant = PinkOnDark,
    error = AppError,
    onError = PinkOnLight
)

@Composable
fun MyApplicationTheme(
    pinkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (pinkTheme) PinkColorScheme else DarkColorScheme,
        typography = Typography,
        content = content
    )
}
