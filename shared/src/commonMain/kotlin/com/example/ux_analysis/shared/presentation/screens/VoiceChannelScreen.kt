package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.domain.models.EstadoUsuario
import com.example.ux_analysis.shared.presentation.components.AvatarConEstado
import com.example.ux_analysis.shared.presentation.components.VoiceChannelEmptyState
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

@Composable
fun VoiceChannelScreen(
    nombreCanal: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isConnected by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DiscordColors.ChatBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                color = DiscordColors.ChatBackground,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "◀",
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .padding(4.dp),
                        color = DiscordColors.TextSecondary
                    )
                    
                    Text(
                        text = "🔊",
                        fontSize = 20.sp,
                        color = DiscordColors.TextSecondary
                    )
                    
                    Text(
                        text = nombreCanal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiscordColors.TextPrimary
                    )
                }
            }

            if (!isConnected) {
                VoiceChannelEmptyState(
                    nombreCanal = nombreCanal,
                    onJoin = { isConnected = true }
                )
            } else {
                // Mock "connected" state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(listOf("Alex", "María", "Erick:)")) { nombre ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    AvatarConEstado(
                                        iniciales = if (nombre == "Alex") "👨" else if (nombre == "María") "👩" else "👤",
                                        estado = EstadoUsuario.ONLINE,
                                        tamaño = 80
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = nombre, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        
                        Surface(
                            modifier = Modifier
                                .clickable { isConnected = false }
                                .padding(vertical = 16.dp),
                            color = DiscordColors.Error,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "Desconectarse",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
