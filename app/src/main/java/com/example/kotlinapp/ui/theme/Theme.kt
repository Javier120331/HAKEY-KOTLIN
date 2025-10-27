package com.example.kotlinapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    // Paleta de colores púrpura estilo ENEBA
    primary = Color(0xFF6A2DE6),        // púrpura vívido
    secondary = Color(0xFF9B51FF),      // púrpura secundario brillante
    tertiary = Color(0xFFB388FF),       // púrpura suave/pastel
    background = Color(0xFFF4EEFF),     // fondo púrpura muy claro
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
