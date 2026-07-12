package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

@Composable
fun ProfileSettingsScreen(
    initialProfile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf(initialProfile.username) }
    var statusText by remember { mutableStateOf(initialProfile.statusText) }
    var presenceStatus by remember { mutableStateOf(initialProfile.presenceStatus) }

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
                            .clickable { onBack() }
                            .padding(4.dp),
                        color = DiscordColors.TextSecondary
                    )
                    Text(
                        text = "Ajustes de Perfil",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiscordColors.TextPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AvatarConEstado(
                    iniciales = "👤",
                    estado = presenceStatus,
                    tamaño = 100
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("NOMBRE DE USUARIO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiscordColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = username,
                        onValueChange = { username = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DiscordColors.RailBackground, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("MENSAJE DE ESTADO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiscordColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = statusText,
                        onValueChange = { statusText = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DiscordColors.RailBackground, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("ESTADO DE PRESENCIA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiscordColors.TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        EstadoUsuario.values().forEach { status ->
                            val color = when (status) {
                                EstadoUsuario.ONLINE -> DiscordColors.StatusOnline
                                EstadoUsuario.IDLE -> DiscordColors.StatusIdle
                                EstadoUsuario.DND -> DiscordColors.StatusDND
                                EstadoUsuario.OFFLINE -> DiscordColors.StatusOffline
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (presenceStatus == status) DiscordColors.ElevatedBackground else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { presenceStatus = status }
                                    .padding(8.dp)
                                    .background(color, CircleShape)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSave(UserProfile(username, statusText, presenceStatus)) },
                    color = DiscordColors.Blurple,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Guardar cambios",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
