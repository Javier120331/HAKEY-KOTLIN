package com.example.kotlinapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    // ENEBA-like purple palette
    primary = Color(0xFF6A2DE6),        // vivid purple
    secondary = Color(0xFF9B51FF),      // bright secondary purple
    tertiary = Color(0xFFB388FF),       // soft/pastel purple
    background = Color(0xFFF4EEFF),     // very light purple background
    surface = Color.White,
    error = Color(0xFFCF6679)
)

@Composable
fun KotlinAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
