package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.domain.models.ChannelPurpose
import com.example.ux_analysis.shared.domain.models.GrupoMensajes
import com.example.ux_analysis.shared.domain.models.TipoCanal
import com.example.ux_analysis.shared.presentation.components.ChannelPresentationPrompt
import com.example.ux_analysis.shared.presentation.components.ChannelWelcomeHeader
import com.example.ux_analysis.shared.presentation.components.MensajeItem
import com.example.ux_analysis.shared.presentation.theme.DiscordColors
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

@Composable
fun ChatScreen(
    gruposMensajes: List<GrupoMensajes>,
    nombreCanal: String,
    tipo: TipoCanal,
    purpose: ChannelPurpose,
    iconoEmoji: String,
    onBackClick: () -> Unit,
    onToggleMembers: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }
    var isEmojiPickerOpen by remember { mutableStateOf(false) }
    val isReadOnly = tipo == TipoCanal.ANUNCIOS

    val commonEmojis = listOf(
        "😀", "😂", "🤣", "😍", "😒", "😎", "😭", "😩", "🤔", "🙄",
        "🔥", "✨", "🎉", "💯", "👍", "👎", "👏", "🙌", "🙏", "👀",
        "❤️", "💔", "💖", "💙", "💚", "💛", "💜", "🧡", "🖤", "✅",
        "❌", "⚠️", "🚀", "🤖", "🎨", "📷", "🎮", "👋", "🏠", "📢"
    )

    // Scroll to bottom when new messages arrive
    LaunchedEffect(gruposMensajes.size, if (gruposMensajes.isNotEmpty()) gruposMensajes.last().mensajes.size else 0) {
        if (gruposMensajes.isNotEmpty()) {
            listState.animateScrollToItem(gruposMensajes.size - 1)
        }
    }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = DiscordColors.ChatBackground
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            // Header mejorado
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
                        text = if (tipo == TipoCanal.ANUNCIOS) "📢" else "#",
                        fontSize = 20.sp,
                        color = DiscordColors.TextSecondary,
                        fontWeight = FontWeight.Light
                    )
                    
                    Text(
                        text = nombreCanal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiscordColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Iconos de utilidad
                    Text(text = "🔔", modifier = Modifier.clickable { })
                    Text(text = "📌", modifier = Modifier.clickable { })
                    Text(text = "👥", modifier = Modifier.clickable { onToggleMembers() })
                    
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(DiscordColors.RailBackground)
                            .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("Buscar", fontSize = 12.sp, color = DiscordColors.TextMuted)
                    }
                }
            }
            
            // Messages list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState,
                reverseLayout = false
            ) {
                // Specialized headers
                item {
                    if (purpose == ChannelPurpose.WELCOME) {
                        ChannelWelcomeHeader(nombreCanal = nombreCanal, iconoEmoji = iconoEmoji)
                    } else if (purpose == ChannelPurpose.PRESENTATION) {
                        ChannelPresentationPrompt()
                    }
                }

                items(
                    items = gruposMensajes,
                    key = { grupo -> grupo.autorId + (grupo.mensajes.firstOrNull()?.id ?: "empty") }
                ) { grupo ->
                    for ((index, mensaje) in grupo.mensajes.withIndex()) {
                        val esPrimerMensaje = index == 0
                        val timestamp = formatTimestamp(mensaje.timestamp)
                        val reaccionesText = if (mensaje.reacciones.isNotEmpty()) {
                            mensaje.reacciones.joinToString(" ") { "${it.emoji} ${it.cantidad}" }
                        } else {
                            ""
                        }

                        MensajeItem(
                            autorNombre = grupo.autorNombre,
                            avatarEmoji = grupo.avatarUrl,
                            contenido = mensaje.contenido,
                            timestamp = timestamp,
                            reaccionesText = reaccionesText,
                            estado = grupo.estado,
                            esPrimerMensaje = esPrimerMensaje
                        )
                    }
                }
            }
            
            // Emoji Picker Panel
            AnimatedVisibility(visible = isEmojiPickerOpen) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    color = DiscordColors.ElevatedBackground
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Emojis", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                text = "✕",
                                color = DiscordColors.TextSecondary,
                                modifier = Modifier.clickable { isEmojiPickerOpen = false }
                            )
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(40.dp),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(commonEmojis) { emoji ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            messageText += emoji
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 24.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Input area mejorado con funcionalidad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 44.dp),
                    color = if (isReadOnly) DiscordColors.RailBackground else DiscordColors.ElevatedBackground,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isReadOnly) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Solo miembros con permisos pueden escribir aquí",
                                fontSize = 14.sp,
                                color = DiscordColors.TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "⊕", fontSize = 20.sp, color = DiscordColors.TextSecondary)
                            
                            Box(modifier = Modifier.weight(1f)) {
                                if (messageText.isEmpty()) {
                                    Text(
                                        text = "Enviar mensaje a #$nombreCanal",
                                        fontSize = 14.sp,
                                        color = DiscordColors.TextMuted
                                    )
                                }
                                
                                androidx.compose.foundation.text.BasicTextField(
                                    value = messageText,
                                    onValueChange = { messageText = it },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                        onSend = {
                                            if (messageText.isNotBlank()) {
                                                onSendMessage(messageText)
                                                messageText = ""
                                                isEmojiPickerOpen = false
                                            }
                                        }
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        color = DiscordColors.TextPrimary,
                                        fontSize = 14.sp
                                    ),
                                    cursorBrush = SolidColor(DiscordColors.TextPrimary),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (messageText.isNotBlank()) {
                                Text(
                                    text = "➤",
                                    color = DiscordColors.Blurple,
                                    modifier = Modifier.clickable {
                                        onSendMessage(messageText)
                                        messageText = ""
                                        isEmojiPickerOpen = false
                                    }
                                )
                            }
                            
                            Text(text = "🎁", fontSize = 18.sp)
                            Text(text = "GIF", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "☺", 
                                fontSize = 18.sp,
                                modifier = Modifier.clickable { isEmojiPickerOpen = !isEmojiPickerOpen }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Ahora"
        diff < 3600000 -> "${diff / 60000}m"
        diff < 86400000 -> "${diff / 3600000}h"
        diff < 604800000 -> "${diff / 86400000}d"
        else -> {
            val format = SimpleDateFormat("d MMM", Locale("es", "ES"))
            format.format(Date(timestamp))
        }
    }
}
