package com.example.ux_analysis.shared.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

object DiscordColors {
    // Colores de marca
    val Blurple = Color(0xFF5865F2)
    
    // Fondos (escala oscura)
    val RailBackground = Color(0xFF1E1F22)
    val ChannelListBackground = Color(0xFF2B2D31)
    val ChatBackground = Color(0xFF313338)
    val ElevatedBackground = Color(0xFF383A40)
    
    // Estados de usuario
    val StatusOnline = Color(0xFF23A55A)
    val StatusIdle = Color(0xFFF0B232)
    val StatusDND = Color(0xFFF23F42)
    val StatusOffline = Color(0xFF80848E)
    
    // Texto
    val TextPrimary = Color(0xFFF2F3F5)
    val TextSecondary = Color(0xFFB5BAC1)
    val TextMuted = Color(0xFF949BA4)
    
    // Estados
    val Success = StatusOnline
    val Warning = StatusIdle
    val Error = StatusDND
}

val DiscordDarkColorScheme = darkColorScheme(
    primary = DiscordColors.Blurple,
    secondary = DiscordColors.StatusOnline,
    tertiary = DiscordColors.StatusDND,
    background = DiscordColors.ChatBackground,
    surface = DiscordColors.ElevatedBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DiscordColors.TextPrimary,
    onSurface = DiscordColors.TextPrimary
)
