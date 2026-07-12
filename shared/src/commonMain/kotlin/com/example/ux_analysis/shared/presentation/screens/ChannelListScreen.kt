package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.domain.models.Canal
import com.example.ux_analysis.shared.domain.models.Categoria
import com.example.ux_analysis.shared.domain.models.TipoCanal
import com.example.ux_analysis.shared.presentation.components.CanalItem
import com.example.ux_analysis.shared.presentation.components.CategoryHeader
import com.example.ux_analysis.shared.presentation.components.NotificationBadge
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.example.ux_analysis.shared.presentation.components.AvatarConEstado
import com.example.ux_analysis.shared.domain.models.EstadoUsuario

@Composable
fun ChannelListScreen(
    serverName: String,
    categorias: List<Categoria>,
    userProfile: UserProfile,
    onCanalSelected: (Canal) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedStates = remember {
        mutableStateOf(categorias.associate { it.id to it.isExpanded })
    }
    
    // Calcular total de canales sin leer
    val countNoLeidos = remember(categorias) {
        categorias.flatMap { it.canales }.count { it.tieneNoLeidos }
    }
    
    // Crear una lista plana de items (categorías y canales)
    val flatItems = remember(categorias, expandedStates.value) {
        val items = mutableListOf<Any>()
        categorias.forEach { categoria ->
            items.add(categoria)
            if (expandedStates.value[categoria.id] == true) {
                items.addAll(categoria.canales)
            }
        }
        items
    }
    
    Surface(
        modifier = modifier
            .width(240.dp)
            .fillMaxHeight(),
        color = DiscordColors.ChannelListBackground
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            // Header con nombre del servidor y contador global
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                color = DiscordColors.ChannelListBackground,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = serverName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiscordColors.TextPrimary
                    )
                    
                    Text(text = "▼", fontSize = 10.sp, color = DiscordColors.TextSecondary)
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(
                    items = flatItems,
                    key = { item ->
                        when (item) {
                            is Categoria -> "cat_${item.id}"
                            is Canal -> "canal_${item.id}"
                            else -> item.hashCode().toString()
                        }
                    }
                ) { item ->
                    when (item) {
                        is Categoria -> {
                            val isExpanded = expandedStates.value[item.id] ?: true
                            CategoryHeader(
                                nombre = item.nombre,
                                isExpanded = isExpanded,
                                onClick = {
                                    expandedStates.value = expandedStates.value.toMutableMap().apply {
                                        this[item.id] = !isExpanded
                                    }
                                }
                            )
                        }
                        is Canal -> {
                            CanalItem(
                                nombre = item.nombre,
                                tipo = item.iconoEmoji,
                                isPrivado = item.esPrivado,
                                tieneNotificaciones = item.tieneNotificaciones,
                                conteoNotificaciones = item.conteoNotificaciones,
                                tieneNoLeidos = item.tieneNoLeidos,
                                onClick = { onCanalSelected(item) }
                            )
                        }
                    }
                }
            }

            // User Profile Section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { onProfileClick() },
                color = DiscordColors.RailBackground
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AvatarConEstado(
                        iniciales = "👤",
                        estado = userProfile.presenceStatus,
                        tamaño = 32
                    )
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.username,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiscordColors.TextPrimary
                        )
                        Text(
                            text = userProfile.statusText,
                            fontSize = 11.sp,
                            color = DiscordColors.TextSecondary
                        )
                    }
                    
                    Text(text = "🎤", fontSize = 16.sp, modifier = Modifier.clickable { })
                    Text(text = "🎧", fontSize = 16.sp, modifier = Modifier.clickable { })
                    Text(text = "⚙️", fontSize = 16.sp, modifier = Modifier.clickable { })
                }
            }
        }
    }
}
