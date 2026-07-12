package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.domain.models.Servidor
import com.example.ux_analysis.shared.presentation.components.ServerIconButton
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun ServerRailScreen(
    servidores: List<Servidor>,
    selectedServerId: String?,
    onServerSelected: (Servidor) -> Unit,
    onCreateServerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredServers = remember(servidores, searchQuery) {
        if (searchQuery.isBlank()) servidores
        else servidores.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
    }

    Surface(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight(),
        color = DiscordColors.RailBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botón de Inicio/Discord Logo
            ServerIconButton(
                icono = "🎮",
                nombre = "Discord",
                isActivo = selectedServerId == null,
                tieneNotificaciones = false,
                conteoNotificaciones = 0,
                onClick = { /* Navegar a DM's */ }
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Botón de búsqueda (Toggle)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSearchVisible) DiscordColors.Blurple else DiscordColors.ElevatedBackground)
                    .clickable { 
                        isSearchVisible = !isSearchVisible 
                        if (!isSearchVisible) searchQuery = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔍", fontSize = 20.sp)
            }

            AnimatedVisibility(
                visible = isSearchVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier
                            .width(56.dp)
                            .background(DiscordColors.ChatBackground, RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Separador
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(DiscordColors.ElevatedBackground)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = filteredServers,
                    key = { servidor -> servidor.id }
                ) { servidor ->
                    ServerIconButton(
                        icono = servidor.avatarUrl,
                        nombre = servidor.nombre,
                        isActivo = servidor.id == selectedServerId,
                        tieneNotificaciones = servidor.tieneNotificaciones,
                        conteoNotificaciones = servidor.conteoNotificaciones,
                        onClick = { onServerSelected(servidor) }
                    )
                }
                
                item {
                    // Botón Añadir Servidor
                    ServerIconButton(
                        icono = "+",
                        nombre = "Añadir",
                        isActivo = false,
                        tieneNotificaciones = false,
                        conteoNotificaciones = 0,
                        onClick = onCreateServerClick
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
