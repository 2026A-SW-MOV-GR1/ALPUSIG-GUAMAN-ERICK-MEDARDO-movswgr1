package com.example.gestionpaqueteria.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class Paquete(
    val idPaquete: String,
    val remitente: String,
    val destinatario: String,
    val direccionOrigen: String,
    val latOrigen: Double,
    val lngOrigen: Double,
    val fechaAdmision: String,
    val estado: String = "ADMITIDO"
) {
    companion object {
        fun createNew(
            remitente: String,
            destinatario: String,
            direccionOrigen: String,
            lat: Double,
            lng: Double
        ): Paquete {
            val now = Clock.System.now()
            val timestamp = now.toEpochMilliseconds()
            val isoDate = now.toString()
            
            return Paquete(
                idPaquete = "AMZ-$timestamp",
                remitente = remitente,
                destinatario = destinatario,
                direccionOrigen = direccionOrigen,
                latOrigen = lat,
                lngOrigen = lng,
                fechaAdmision = isoDate,
                estado = "ADMITIDO"
            )
        }
    }
}
