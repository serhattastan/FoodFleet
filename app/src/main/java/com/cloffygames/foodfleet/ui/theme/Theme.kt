package com.cloffygames.foodfleet.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Dark and Light color schemes based on your static color definitions
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryTextColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryTextColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onBackground = PrimaryTextColor,
    onSurface = PrimaryTextColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryTextColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryTextColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onBackground = PrimaryTextColor,
    onSurface = PrimaryTextColor
)

@Composable
fun FoodFleetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}