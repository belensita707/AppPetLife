package com.example.apppetlife.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val PetLifeLightColorScheme = lightColorScheme(
    primary = SoftTeal,
    secondary = SoftPink,
    background = Cream,
    surface = LightGray,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
)

@Composable
fun PetLifeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PetLifeLightColorScheme,
        typography = Typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
