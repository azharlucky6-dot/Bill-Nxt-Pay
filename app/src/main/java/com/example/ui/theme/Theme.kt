package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class NxtPayExtendedColors(
    val shellBgTop: Color,
    val shellBgMid: Color,
    val shellBgBottom: Color,
    val cardBorder: Color,
    val subtleSurface: Color,
    val tileSurface: Color,
    val brandIndigo: Color,
    val accentIndigo: Color,
    val accentIndigoBg: Color,
    val amberBg: Color,
    val amberBgEnd: Color,
    val amberBorder: Color,
    val amberText: Color,
    val amberIcon: Color,
    val emeraldBgStart: Color,
    val emeraldBgEnd: Color,
    val emeraldBorder: Color,
    val emeraldText: Color,
    val emeraldIcon: Color,
    val roseBgStart: Color,
    val roseBgEnd: Color,
    val roseBorder: Color,
    val roseText: Color,
    val mutedText: Color,
    val navBarSurface: Color
)

private val LightExtendedColors = NxtPayExtendedColors(
    shellBgTop = ShellBgTop,
    shellBgMid = ShellBgMid,
    shellBgBottom = ShellBgBottom,
    cardBorder = BorderSlate200,
    subtleSurface = SurfaceSlate50,
    tileSurface = SurfaceSlate100,
    brandIndigo = NxtRoyalIndigo,
    accentIndigo = NxtIndigoAccent,
    accentIndigoBg = NxtIndigoSoft,
    amberBg = Amber50,
    amberBgEnd = Orange50,
    amberBorder = Amber200,
    amberText = Amber800,
    amberIcon = Amber600,
    emeraldBgStart = Emerald50,
    emeraldBgEnd = Teal50,
    emeraldBorder = Emerald200,
    emeraldText = Emerald900,
    emeraldIcon = Emerald700,
    roseBgStart = Rose50,
    roseBgEnd = Orange50,
    roseBorder = Rose200,
    roseText = Rose800,
    mutedText = TextSlate500,
    navBarSurface = SurfaceWhite.copy(alpha = 0.96f)
)

private val DarkExtendedColors = NxtPayExtendedColors(
    shellBgTop = DarkBgTop,
    shellBgMid = DarkBgMid,
    shellBgBottom = DarkBgBottom,
    cardBorder = DarkBorder,
    subtleSurface = DarkSurfaceVariant,
    tileSurface = DarkSurfaceVariant,
    brandIndigo = NxtRoyalIndigo,
    accentIndigo = DarkIndigoPrimary,
    accentIndigoBg = Color(0xFF1E2547),
    amberBg = Color(0xFF2B2110),
    amberBgEnd = Color(0xFF2E1E12),
    amberBorder = Color(0xFF5A4018),
    amberText = Amber200,
    amberIcon = Amber500,
    emeraldBgStart = Color(0xFF0E261F),
    emeraldBgEnd = Color(0xFF0F2927),
    emeraldBorder = Color(0xFF1E5645),
    emeraldText = Emerald300,
    emeraldIcon = Emerald400,
    roseBgStart = Color(0xFF2E141B),
    roseBgEnd = Color(0xFF2C1914),
    roseBorder = Color(0xFF642636),
    roseText = Rose300,
    mutedText = DarkTextSecondary,
    navBarSurface = DarkSurface.copy(alpha = 0.96f)
)

val LocalNxtPayColors = staticCompositionLocalOf { LightExtendedColors }

private val LightColorScheme = lightColorScheme(
    primary = NxtRoyalIndigo,
    onPrimary = SurfaceWhite,
    primaryContainer = NxtIndigoSoft,
    onPrimaryContainer = NxtIndigoDark,
    secondary = NxtIndigoAccent,
    onSecondary = SurfaceWhite,
    secondaryContainer = SurfaceSlate100,
    onSecondaryContainer = TextSlate900,
    tertiary = Amber600,
    onTertiary = SurfaceWhite,
    background = ShellBgMid,
    onBackground = TextSlate900,
    surface = SurfaceWhite,
    onSurface = TextSlate900,
    surfaceVariant = SurfaceSlate50,
    onSurfaceVariant = TextSlate600,
    outline = BorderSlate200,
    outlineVariant = BorderSlate300,
    error = Rose500,
    onError = SurfaceWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkIndigoPrimary,
    onPrimary = Color(0xFF0A0E24),
    primaryContainer = NxtRoyalIndigo,
    onPrimaryContainer = SurfaceWhite,
    secondary = Amber500,
    onSecondary = Color(0xFF1E1302),
    background = DarkBgMid,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
    error = Rose400,
    onError = Color(0xFF2D060E)
)

val NxtPayShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(26.dp)
)

object NxtPayTheme {
    val extended: NxtPayExtendedColors
        @Composable get() = LocalNxtPayColors.current
}

@Composable
fun NxtPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalNxtPayColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = NxtPayTypography,
            shapes = NxtPayShapes,
            content = content
        )
    }
}
