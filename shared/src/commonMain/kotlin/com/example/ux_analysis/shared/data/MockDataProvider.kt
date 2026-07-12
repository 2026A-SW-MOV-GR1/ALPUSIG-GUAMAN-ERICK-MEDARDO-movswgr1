package com.example.ux_analysis.shared.data

import com.example.ux_analysis.shared.domain.models.*

object MockDataProvider {
    
    fun getMockServidores(): List<Servidor> = listOf(
        Servidor("1", "Home", "🏠", isActivo = true),
        Servidor("2", "Gaming", "🎮", tieneNotificaciones = true, conteoNotificaciones = 3),
        Servidor("3", "Bootcamp", "🤖")
    )
    
    fun getMockCategorias(): List<Categoria> = listOf(
        Categoria(
            id = "cat_inicio",
            nombre = "INICIO",
            isExpanded = true,
            canales = listOf(
                Canal("ch_bienvenida", "bienvenida", TipoCanal.TEXTO, iconoEmoji = "🏠", purpose = ChannelPurpose.WELCOME),
                Canal("ch_anuncios_inicio", "anuncios", TipoCanal.ANUNCIOS, iconoEmoji = "📢", tieneNoLeidos = true)
            )
        ),
        Categoria(
            id = "cat_escribamos",
            nombre = "ESCRIBAMOS",
            isExpanded = true,
            canales = listOf(
                Canal("ch_general", "general", TipoCanal.TEXTO, iconoEmoji = "💬"),
                Canal("ch_imagenes", "imágenes", TipoCanal.GALERIA, iconoEmoji = "📷"),
                Canal("ch_presentacion", "presentación", TipoCanal.TEXTO, iconoEmoji = "👋", purpose = ChannelPurpose.PRESENTATION),
                Canal("ch_memes", "memes", TipoCanal.TEXTO, iconoEmoji = "😂")
            )
        ),
        Categoria(
            id = "cat_voice",
            nombre = "VOICE-CHATS",
            isExpanded = true,
            canales = listOf(
                Canal("ch_vc_general", "VC-General", TipoCanal.VOZ, iconoEmoji = "🔊"),
                Canal("ch_vc_gaming", "Gaming Room", TipoCanal.VOZ, iconoEmoji = "🔊")
            )
        )
    )
    
    fun getMockMensajes(): List<GrupoMensajes> = getMessagesForChannel("ch_general")

    fun getMessagesForChannel(channelId: String): List<GrupoMensajes> {
        val ahora = System.currentTimeMillis()
        
        return when (channelId) {
            "ch_bienvenida" -> listOf(
                GrupoMensajes("system", "System", "🤖", EstadoUsuario.ONLINE, listOf(
                    Mensaje("w1", "system", "System", "🤖", "¡Bienvenido/a a Gaming Legends! 🎉", ahora - 3600000)
                )),
                GrupoMensajes("user1", "Alex", "👨", EstadoUsuario.ONLINE, listOf(
                    Mensaje("w2", "user1", "Alex", "👨", "Hola a todos, ¡qué bueno estar aquí!", ahora - 1800000)
                ))
            )
            "ch_anuncios_inicio" -> listOf(
                GrupoMensajes("admin", "Admin", "🛡️", EstadoUsuario.ONLINE, listOf(
                    Mensaje("a1", "admin", "Admin", "🛡️", "📌 Reglas actualizadas. Por favor léanlas en el canal de normas.", ahora - 7200000),
                    Mensaje("a2", "admin", "Admin", "🛡️", "📢 ¡Nuevo torneo de KMP anunciado para el viernes!", ahora - 3600000)
                ))
            )
            "ch_general" -> listOf(
                GrupoMensajes("user1", "Alex", "👨", EstadoUsuario.ONLINE, listOf(
                    Mensaje("g1", "user1", "Alex", "👨", "¿Alguien para jugar unas partidas?", ahora - 600000)
                )),
                GrupoMensajes("user2", "María", "👩", EstadoUsuario.ONLINE, listOf(
                    Mensaje("g2", "user2", "María", "👩", "¡Yo me apunto! Solo termino este commit.", ahora - 300000)
                ))
            )
            "ch_presentacion" -> listOf(
                GrupoMensajes("user3", "Carlos", "👨‍💻", EstadoUsuario.IDLE, listOf(
                    Mensaje("p1", "user3", "Carlos", "👨‍💻", "Hola, soy Carlos de Argentina. Me encanta el desarrollo móvil.", ahora - 86400000)
                )),
                GrupoMensajes("user2", "María", "👩", EstadoUsuario.ONLINE, listOf(
                    Mensaje("p2", "user2", "María", "👩", "¡Bienvenido Carlos! Soy María, UX Designer.", ahora - 43200000)
                ))
            )
            "ch_memes" -> listOf(
                GrupoMensajes("user1", "Alex", "👨", EstadoUsuario.ONLINE, listOf(
                    Mensaje("m1", "user1", "Alex", "👨", "Cuando el build falla pero el logcat no dice nada...", ahora - 10000, listOf(
                        Reaccion("😂", 5, true),
                        Reaccion("💀", 2, false)
                    ))
                ))
            )
            else -> listOf(
                GrupoMensajes("system", "System", "🤖", EstadoUsuario.ONLINE, listOf(
                    Mensaje("def1", "system", "System", "🤖", "Este es el comienzo del canal #${channelId}.", ahora)
                ))
            )
        }
    }
    
    fun getMockMiembros(): List<Miembro> = listOf(
        Miembro("user1", "Alex", "👨", EstadoUsuario.ONLINE, null),
        Miembro("user2", "María", "👩", EstadoUsuario.ONLINE, null),
        Miembro("user3", "Carlos", "👨‍💻", EstadoUsuario.IDLE, null)
    )
}
