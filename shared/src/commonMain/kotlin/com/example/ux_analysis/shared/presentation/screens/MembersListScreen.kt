package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.domain.models.EstadoUsuario
import com.example.ux_analysis.shared.domain.models.Miembro
import com.example.ux_analysis.shared.presentation.components.MiembroItem
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MembersListScreen(
    miembros: List<Miembro>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Agrupar miembros por estado
    val miembrosPorEstado = miembros.groupBy { it.estado }
    val estados = listOf(
        EstadoUsuario.ONLINE,
        EstadoUsuario.IDLE,
        EstadoUsuario.DND,
        EstadoUsuario.OFFLINE
    )
    
    Surface(
        modifier = modifier
            .width(240.dp)
            .fillMaxHeight(),
        color = DiscordColors.ChannelListBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header con botón de cerrar para mobile overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Miembros",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DiscordColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "✕",
                    modifier = Modifier.clickable { onClose() },
                    color = DiscordColors.TextSecondary
                )
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                estados.forEach { estado ->
                    val miembrosDelEstado = miembrosPorEstado[estado] ?: emptyList()
                    
                    if (miembrosDelEstado.isNotEmpty()) {
                        item {
                            Text(
                                text = "${estado.name} — ${miembrosDelEstado.size}",
                                fontSize = 12.sp,
                                color = DiscordColors.TextSecondary,
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                            )
                        }
                        
                        items(
                            items = miembrosDelEstado,
                            key = { miembro -> miembro.id }
                        ) { miembro ->
                            MiembroItem(
                                nombre = miembro.nombre,
                                avatarEmoji = miembro.avatarUrl,
                                estado = miembro.estado,
                                actividad = miembro.actividad
                            )
                        }
                    }
                }
            }
        }
    }
}
