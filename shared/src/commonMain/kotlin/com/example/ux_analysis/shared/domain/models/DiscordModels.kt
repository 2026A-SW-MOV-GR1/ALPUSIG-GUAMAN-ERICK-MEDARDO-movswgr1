package com.example.ux_analysis.shared.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Servidor(
    val id: String,
    val nombre: String,
    val avatarUrl: String,
    val isActivo: Boolean = false,
    val tieneNotificaciones: Boolean = false,
    val conteoNotificaciones: Int = 0
)

@Serializable
enum class TipoCanal {
    TEXTO, VOZ, FORUM, ANUNCIOS, GALERIA
}

@Serializable
enum class ChannelPurpose {
    WELCOME, PRESENTATION, NONE
}

@Serializable
data class Canal(
    val id: String,
    val nombre: String,
    val tipo: TipoCanal = TipoCanal.TEXTO,
    val iconoEmoji: String = "#",
    val purpose: ChannelPurpose = ChannelPurpose.NONE,
    val esPrivado: Boolean = false,
    val tieneNotificaciones: Boolean = false,
    val conteoNotificaciones: Int = 0,
    val categoriaPadreId: String? = null,
    val tieneNoLeidos: Boolean = false,
    val tiempoUltimoMensaje: Long = 0
)

@Serializable
data class Categoria(
    val id: String,
    val nombre: String,
    val isExpanded: Boolean = true,
    val canales: List<Canal> = emptyList()
)

@Serializable
enum class EstadoUsuario(val hex: String) {
    ONLINE("#23A55A"),
    IDLE("#F0B232"),
    DND("#F23F42"),
    OFFLINE("#80848E")
}

@Serializable
data class Miembro(
    val id: String,
    val nombre: String,
    val avatarUrl: String,
    val estado: EstadoUsuario = EstadoUsuario.OFFLINE,
    val actividad: String? = null,
    val rolColor: Long? = null
)

@Serializable
data class Mensaje(
    val id: String,
    val autorId: String,
    val autorNombre: String,
    val avatarUrl: String,
    val contenido: String,
    val timestamp: Long,
    val reacciones: List<Reaccion> = emptyList(),
    val esRespuesta: Boolean = false,
    val mensajeReferenciado: Mensaje? = null
)

@Serializable
data class Reaccion(
    val emoji: String,
    val cantidad: Int,
    val yoReaccione: Boolean = false
)

@Serializable
data class GrupoMensajes(
    val autorId: String,
    val autorNombre: String,
    val avatarUrl: String,
    val estado: EstadoUsuario,
    val mensajes: List<Mensaje> = emptyList()
)
