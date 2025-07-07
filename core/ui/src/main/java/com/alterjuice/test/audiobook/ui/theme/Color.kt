package com.alterjuice.test.audiobook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color


data object CustomColors {
    val PlayerBlue = Color(0xFF0D6BFF)
    val PlayerDarkBlue = Color(0xFF004CB3)
    val PlayerBackground = Color(0xFFF8F9FA)
    val PlayerTextPrimary = Color(0xFF1C1B1F)
    val PlayerTextSecondary = Color(0xFF6C757D)
    val PlayerSliderInactive = Color(0xFFDDE3F0)
    val PlayerButtonBackground = Color(0xFFF1F3F5)
}

val MaterialTheme.customScheme get() = CustomColors