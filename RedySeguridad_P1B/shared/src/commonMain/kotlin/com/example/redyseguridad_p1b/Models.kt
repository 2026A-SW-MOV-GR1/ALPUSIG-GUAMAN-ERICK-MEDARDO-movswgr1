package com.example.redyseguridad_p1b

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

enum class StorageType {
    SHARED_PREFERENCES,
    DATA_STORE,
    ENCRYPTED_SHARED_PREFERENCES
}

data class SecretEntry(
    val key: String,
    val value: String,
    val storage: StorageType
)

