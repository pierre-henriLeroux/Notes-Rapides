package fr.phlab.notesrapides.utils.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration


private val LocalAppDimens = staticCompositionLocalOf {
    compactDimensions
}

private val LocalAppColors = staticCompositionLocalOf {
    DarkAppColors
}


object Theme {
    val dimens: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimens.current
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}



@Composable
fun ThemeProvider(content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    CompositionLocalProvider(
        LocalAppDimens provides if (configuration.screenWidthDp < 600) {
            compactDimensions
        } else {
            largeDimensions
        },
        LocalAppColors provides if (isSystemInDarkTheme()) {
            DarkAppColors
        } else {
            LightAppColors
        }
    ) {
        content()
    }
}

