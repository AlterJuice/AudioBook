package com.alterjuice.test.audiobook.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = MaterialTheme.customScheme.PlayerBlue,
    background = MaterialTheme.customScheme.PlayerBackground,
    surface = MaterialTheme.customScheme.PlayerBackground,
    onPrimary = Color.White,
    tertiary = Color.Unspecified,
    // secondaryContainer is used as bg for FilledTonalButton and Selector components (inactive parts )
    secondaryContainer = MaterialTheme.customScheme.PlayerSliderInactive,
    onBackground = MaterialTheme.customScheme.PlayerTextPrimary,
    onSurface = MaterialTheme.customScheme.PlayerTextPrimary,
    onSurfaceVariant = MaterialTheme.customScheme.PlayerTextSecondary,
    surfaceVariant = MaterialTheme.customScheme.PlayerSliderInactive
)

@Composable
fun AudioBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}