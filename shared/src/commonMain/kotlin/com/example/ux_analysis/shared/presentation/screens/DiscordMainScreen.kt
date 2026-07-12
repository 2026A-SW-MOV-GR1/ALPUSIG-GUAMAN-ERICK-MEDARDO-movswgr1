package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.ux_analysis.shared.data.MockDataProvider
import com.example.ux_analysis.shared.domain.models.*
import com.example.ux_analysis.shared.presentation.components.*
import com.example.ux_analysis.shared.presentation.theme.DiscordTheme

sealed class AppScreen {
    object Splash : AppScreen()
    object Channels : AppScreen()
    object Chat : AppScreen()
    object Profile : AppScreen()
}

data class UserProfile(
    val username: String,
    val statusText: String,
    val presenceStatus: EstadoUsuario
)

@Composable
fun DiscordMainScreen() {
    DiscordTheme {
        val servidores = remember { MockDataProvider.getMockServidores().toMutableStateList() }
        val categorias = remember { MockDataProvider.getMockCategorias() }
        val miembros = remember { MockDataProvider.getMockMiembros() }
        
        var userProfile by remember { 
            mutableStateOf(UserProfile("Erick:)", "#Online", EstadoUsuario.ONLINE))
        }
        
        // State for messages per channel
        val channelMessages = remember { 
            mutableStateMapOf<String, List<GrupoMensajes>>().apply {
                categorias.forEach { cat ->
                    cat.canales.forEach { canal ->
                        this[canal.id] = MockDataProvider.getMessagesForChannel(canal.id)
                    }
                }
            }
        }

        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }
        var selectedServidor by remember { mutableStateOf(servidores.firstOrNull()) }
        var selectedCanal by remember { mutableStateOf(categorias.firstOrNull()?.canales?.firstOrNull()) }
        var isMemberListOpen by remember { mutableStateOf(false) }
        var isCreateServerOpen by remember { mutableStateOf(false) }
        
        if (currentScreen is AppScreen.Splash) {
            SplashScreen(onFinished = { currentScreen = AppScreen.Channels })
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Rail de servidores - Siempre visible (Fixed Column)
                    ServerRailScreen(
                        servidores = servidores,
                        selectedServerId = selectedServidor?.id,
                        onServerSelected = { servidor ->
                            selectedServidor = servidor
                            currentScreen = AppScreen.Channels
                        },
                        onCreateServerClick = { isCreateServerOpen = true }
                    )
                    
                    Box(modifier = Modifier.weight(1f)) {
                        // Panel de Canales, Chat o Perfil (Single Pane)
                        when (currentScreen) {
                            is AppScreen.Channels -> {
                                ChannelListScreen(
                                    serverName = selectedServidor?.nombre ?: "Servidor",
                                    categorias = categorias,
                                    userProfile = userProfile,
                                    onCanalSelected = { canal ->
                                        selectedCanal = canal
                                        currentScreen = AppScreen.Chat
                                    },
                                    onProfileClick = { currentScreen = AppScreen.Profile }
                                )
                            }
                            is AppScreen.Chat -> {
                                val canalId = selectedCanal?.id ?: ""
                                val mensajes = channelMessages[canalId] ?: emptyList()
                                val currentCanal = selectedCanal
                                
                                if (currentCanal?.tipo == TipoCanal.VOZ) {
                                    VoiceChannelScreen(
                                        nombreCanal = currentCanal.nombre,
                                        onBackClick = { currentScreen = AppScreen.Channels },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else if (currentCanal?.tipo == TipoCanal.GALERIA) {
                                    GalleryChannelScreen(
                                        nombreCanal = currentCanal.nombre,
                                        onBackClick = { currentScreen = AppScreen.Channels },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    ChatScreen(
                                        gruposMensajes = mensajes,
                                        nombreCanal = currentCanal?.nombre ?: "general",
                                        tipo = currentCanal?.tipo ?: TipoCanal.TEXTO,
                                        purpose = currentCanal?.purpose ?: ChannelPurpose.NONE,
                                        iconoEmoji = currentCanal?.iconoEmoji ?: "#",
                                        onBackClick = { currentScreen = AppScreen.Channels },
                                        onToggleMembers = { isMemberListOpen = !isMemberListOpen },
                                        onSendMessage = { text ->
                                            val currentUserId = "me"
                                            val now = System.currentTimeMillis()
                                            val newMensaje = Mensaje(
                                                id = "msg_$now",
                                                autorId = currentUserId,
                                                autorNombre = userProfile.username,
                                                avatarUrl = "👤",
                                                contenido = text,
                                                timestamp = now
                                            )
                                            
                                            val currentList = channelMessages[canalId] ?: emptyList()
                                            val lastGrupo = currentList.lastOrNull()
                                            
                                            if (lastGrupo?.autorId == currentUserId) {
                                                // Update the last group
                                                val updatedGrupo = lastGrupo.copy(
                                                    mensajes = lastGrupo.mensajes + newMensaje
                                                )
                                                channelMessages[canalId] = currentList.toMutableList().apply {
                                                    this[this.size - 1] = updatedGrupo
                                                }
                                            } else {
                                                // Create a new group
                                                channelMessages[canalId] = currentList + GrupoMensajes(
                                                    autorId = currentUserId,
                                                    autorNombre = userProfile.username,
                                                    avatarUrl = "👤",
                                                    estado = userProfile.presenceStatus,
                                                    mensajes = listOf(newMensaje)
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            is AppScreen.Profile -> {
                                ProfileSettingsScreen(
                                    initialProfile = userProfile,
                                    onSave = { updated ->
                                        userProfile = updated
                                        currentScreen = AppScreen.Channels
                                    },
                                    onBack = { currentScreen = AppScreen.Channels },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            else -> {}
                        }
                    }
                }

                if (isCreateServerOpen) {
                    CreateServerDialog(
                        onConfirm = { name, icon ->
                            val newServer = Servidor(
                                id = "srv_${System.currentTimeMillis()}",
                                nombre = name,
                                avatarUrl = if (icon.isNotBlank()) icon else name.take(1)
                            )
                            servidores.add(newServer)
                            isCreateServerOpen = false
                        },
                        onDismiss = { isCreateServerOpen = false }
                    )
                }

                // Member List Overlay
                AnimatedVisibility(
                    visible = isMemberListOpen,
                    enter = slideInHorizontally { it },
                    exit = slideOutHorizontally { it },
                    modifier = Modifier
                        .fillMaxHeight()
                        .zIndex(10f)
                ) {
                    // Background dimmed clicker could be added here
                    Box(modifier = Modifier.fillMaxSize()) {
                        MembersListScreen(
                            miembros = miembros,
                            onClose = { isMemberListOpen = false }
                        )
                    }
                }
            }
        }
    }
}
