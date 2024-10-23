package fr.phlab.notesrapides.utils.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color


@Stable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val surface: Color,
    val onSurface: Color
) {
    val error = Color(0xFFB3261E)
    val onError = Color(0xFFFFFFFF)
    val colorBorder = Color(0xFF696969)
}

val LightAppColors = AppColors(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF000000),
)

val DarkAppColors = AppColors(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    surface = Color(0xFF141218),
    onSurface = Color(0xFFFFFFFF),
)

