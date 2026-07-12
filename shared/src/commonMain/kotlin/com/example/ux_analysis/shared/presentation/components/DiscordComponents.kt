package com.example.ux_analysis.shared.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ux_analysis.shared.domain.models.EstadoUsuario
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

@Composable
fun NotificationBadge(
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    if (cantidad > 0) {
        Surface(
            modifier = modifier
                .size(24.dp),
            shape = CircleShape,
            color = DiscordColors.Error
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (cantidad > 99) "99+" else cantidad.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AvatarConEstado(
    iniciales: String,
    estado: EstadoUsuario,
    tamaño: Int = 48,
    modifier: Modifier = Modifier
) {
    val statusColor = when (estado) {
        EstadoUsuario.ONLINE -> DiscordColors.StatusOnline
        EstadoUsuario.IDLE -> DiscordColors.StatusIdle
        EstadoUsuario.DND -> DiscordColors.StatusDND
        EstadoUsuario.OFFLINE -> DiscordColors.StatusOffline
    }

    Box(
        modifier = modifier.size(tamaño.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            shape = CircleShape,
            color = DiscordColors.ElevatedBackground
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    fontSize = (tamaño / 2.2).sp,
                    fontWeight = FontWeight.Bold,
                    color = DiscordColors.TextPrimary
                )
            }
        }
        
        // Indicador de estado con stroke
        Box(
            modifier = Modifier
                .size((tamaño / 3).dp)
                .align(Alignment.BottomEnd)
                .offset(x = 1.dp, y = 1.dp)
                .background(DiscordColors.ChatBackground, CircleShape)
                .padding(2.dp)
                .background(statusColor, CircleShape)
        )
    }
}

@Composable
fun ServerIconButton(
    icono: String,
    nombre: String,
    isActivo: Boolean,
    tieneNotificaciones: Boolean,
    conteoNotificaciones: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val cornerSize by animateDpAsState(
        targetValue = if (isActivo || isPressed) 16.dp else 28.dp
    )
    
    val indicatorHeight by animateDpAsState(
        targetValue = if (isActivo) 40.dp else if (tieneNotificaciones) 8.dp else 0.dp
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicador lateral (Pill)
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(indicatorHeight)
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .background(Color.White)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            val backgroundColor = if (isActivo) DiscordColors.Blurple else DiscordColors.ChannelListBackground
            
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(cornerSize),
                color = backgroundColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (icono.length <= 2) icono else nombre.take(1),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
            
            if (tieneNotificaciones && !isActivo) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .border(2.dp, DiscordColors.RailBackground, CircleShape),
                    shape = CircleShape,
                    color = DiscordColors.Error
                ) {}
            }
        }
    }
}

@Composable
fun CanalItem(
    nombre: String,
    tipo: String,
    isPrivado: Boolean,
    tieneNotificaciones: Boolean,
    conteoNotificaciones: Int,
    isSelected: Boolean = false,
    tieneNoLeidos: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) DiscordColors.ElevatedBackground else Color.Transparent
    val contentColor = if (isSelected || tieneNoLeidos) DiscordColors.TextPrimary else DiscordColors.TextSecondary
    val fontWeight = if (tieneNoLeidos || isSelected) FontWeight.Bold else FontWeight.Normal

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .height(34.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tipo,
                fontSize = 18.sp,
                color = DiscordColors.TextSecondary,
                fontWeight = FontWeight.Light
            )
            
            Text(
                text = nombre,
                fontSize = 14.sp,
                color = contentColor,
                modifier = Modifier.weight(1f),
                fontWeight = fontWeight
            )
            
            if (tieneNotificaciones) {
                NotificationBadge(cantidad = conteoNotificaciones)
            }
            
            if (isPrivado) {
                Text(text = "🔒", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MensajeItem(
    autorNombre: String,
    avatarEmoji: String,
    contenido: String,
    timestamp: String,
    reaccionesText: String,
    estado: EstadoUsuario,
    esPrimerMensaje: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (esPrimerMensaje) {
            AvatarConEstado(
                iniciales = avatarEmoji,
                estado = estado,
                tamaño = 40
            )
        } else {
            Spacer(modifier = Modifier.width(40.dp))
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            if (esPrimerMensaje) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = autorNombre,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DiscordColors.TextPrimary
                    )
                    Text(
                        text = timestamp,
                        fontSize = 12.sp,
                        color = DiscordColors.TextSecondary
                    )
                }
            }
            
            Text(
                text = contenido,
                fontSize = 14.sp,
                color = DiscordColors.TextPrimary
            )
            
            if (reaccionesText.isNotEmpty()) {
                Text(
                    text = reaccionesText,
                    fontSize = 12.sp,
                    color = DiscordColors.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MiembroItem(
    nombre: String,
    avatarEmoji: String,
    estado: EstadoUsuario,
    actividad: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AvatarConEstado(
            iniciales = avatarEmoji,
            estado = estado,
            tamaño = 32
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = nombre,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DiscordColors.TextPrimary
            )
            if (actividad != null) {
                Text(
                    text = actividad,
                    fontSize = 11.sp,
                    color = DiscordColors.TextSecondary
                )
            }
        }
    }
}

@Composable
fun CategoryHeader(
    nombre: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isExpanded) "▼" else "▶",
                fontSize = 8.sp,
                color = DiscordColors.TextSecondary,
                modifier = Modifier.padding(start = 4.dp)
            )
            
            Text(
                text = nombre,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DiscordColors.TextSecondary,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun ChannelWelcomeHeader(
    nombreCanal: String,
    iconoEmoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = DiscordColors.ElevatedBackground
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = iconoEmoji, fontSize = 48.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "¡Bienvenido a #$nombreCanal!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DiscordColors.TextPrimary
        )
        
        Text(
            text = "Este es el comienzo del canal #$nombreCanal.",
            fontSize = 16.sp,
            color = DiscordColors.TextSecondary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DiscordColors.ElevatedBackground)
        )
    }
}

@Composable
fun ChannelPresentationPrompt(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = DiscordColors.ElevatedBackground,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "👋 Preséntate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DiscordColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cuéntanos un poco sobre ti: ¿cuál es tu nombre y qué te gusta hacer? ¡Estamos felices de tenerte aquí!",
                fontSize = 14.sp,
                color = DiscordColors.TextSecondary
            )
        }
    }
}

@Composable
fun VoiceChannelEmptyState(
    nombreCanal: String,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = DiscordColors.ElevatedBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🔊", fontSize = 64.sp)
                }
            }
            
            Text(
                text = nombreCanal,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DiscordColors.TextPrimary
            )
            
            Text(
                text = "No hay nadie en el chat de voz en este momento",
                fontSize = 14.sp,
                color = DiscordColors.TextSecondary
            )
            
            Surface(
                modifier = Modifier
                    .clickable { onJoin() }
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                color = Color(0xFF23A55A), // StatusOnline color
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Unirse al chat de voz",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CreateServerDialog(
    onConfirm: (name: String, icon: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = DiscordColors.ElevatedBackground
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Personaliza tu servidor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "Dale a tu nuevo servidor una personalidad propia con un nombre y un icono.",
                    fontSize = 14.sp,
                    color = DiscordColors.TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("NOMBRE DEL SERVIDOR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiscordColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DiscordColors.RailBackground, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("ICONO/EMOJI (OPCIONAL)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiscordColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = icon,
                        onValueChange = { if (it.length <= 2) icon = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DiscordColors.RailBackground, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.White,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(16.dp)
                    )
                    
                    Surface(
                        modifier = Modifier.clickable { if (name.isNotBlank()) onConfirm(name, icon) },
                        color = if (name.isNotBlank()) DiscordColors.Blurple else DiscordColors.Blurple.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Crear servidor",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
