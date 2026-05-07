package com.timothy.pesawise.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val GreenPrimary   = Color(0xFF0D3B2E)
val GreenAccent    = Color(0xFF00C896)
val GreenLight     = Color(0xFFE6F7F2)
val GoldYellow     = Color(0xFFF5C842)
val DangerRed      = Color(0xFFFF4D6D)
val MutedGray      = Color(0xFF8A9E96)
val CardWhite      = Color(0xFFFFFFFF)
val BackgroundGray = Color(0xFFF4F7F5)
val DarkBg         = Color(0xFF0F172A)

val SalariedAccent = Color(0xFF00C896)
val BusinessAccent = Color(0xFFF59E0B)
val StudentAccent  = Color(0xFF38BDF8)

private val PesaWiseColorScheme = lightColorScheme(
    primary          = GreenPrimary,
    onPrimary        = Color.White,
    primaryContainer = GreenLight,
    secondary        = GreenAccent,
    onSecondary      = Color.White,
    background       = BackgroundGray,
    surface          = CardWhite,
    onBackground     = Color(0xFF0D1B16),
    onSurface        = Color(0xFF0D1B16),
    error            = DangerRed
)

val PesaTypography = androidx.compose.material3.Typography(
    headlineLarge  = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black),
    headlineMedium = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold),
    headlineSmall  = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
    titleLarge     = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
    titleMedium    = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge      = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal),
    bodyMedium     = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
    bodySmall      = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal),
    labelSmall     = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium)
)

@Composable
fun PesaWiseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PesaWiseColorScheme,
        typography  = PesaTypography,
        content     = content
    )
}
