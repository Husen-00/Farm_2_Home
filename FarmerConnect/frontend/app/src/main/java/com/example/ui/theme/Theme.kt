package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AgriculturalLightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftMintContainer,
    onPrimaryContainer = ForestGreenPrimary,
    secondary = EmeraldGreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = SoftEmeraldSurface,
    onSecondaryContainer = ForestGreenPrimary,
    tertiary = GoldenYellowAccent,
    onTertiary = DarkCharcoalText,
    tertiaryContainer = SoftGoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = WarmCreamBackground,
    onBackground = DarkCharcoalText,
    surface = PureWhiteCard,
    onSurface = DarkCharcoalText,
    surfaceVariant = SoftCreamVariant,
    onSurfaceVariant = GreySecondaryText,
    surfaceContainerLowest = PureWhiteCard,
    surfaceContainerLow = PureWhiteCard,
    surfaceContainer = PureWhiteCard,
    surfaceContainerHigh = SoftCreamVariant,
    surfaceContainerHighest = SoftMintContainer,
    outline = SoftSlateBorder,
    outlineVariant = SoftSlateBorder,
    error = SoftRedError,
    onError = Color.White,
    errorContainer = SoftRedContainer,
    onErrorContainer = OnRedContainer
)

val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Enforces bright, clean light agricultural theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AgriculturalLightColorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
