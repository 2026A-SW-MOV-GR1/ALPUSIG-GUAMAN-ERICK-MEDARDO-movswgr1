package com.example.redyseguridad_p1b

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.first

private const val DATASTORE_NAME = "secret_store"
private const val SHARED_PREFS_NAME = "secrets_sp"
private const val ENCRYPTED_PREFS_NAME = "secrets_esp"

private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class SecretRepositoryImpl(private val context: Context) : SecretRepository {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private val encryptedPrefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun saveSecret(entry: SecretEntry): Result<Unit> = runCatching {
        when (entry.storage) {
            StorageType.SHARED_PREFERENCES -> {
                sharedPrefs.edit().putString(entry.key, entry.value).apply()
            }
            StorageType.DATA_STORE -> {
                val key = stringPreferencesKey(entry.key)
                context.dataStore.edit { prefs ->
                    prefs[key] = entry.value
                }
            }
            StorageType.ENCRYPTED_SHARED_PREFERENCES -> {
                encryptedPrefs.edit().putString(entry.key, entry.value).apply()
            }
        }
    }

    override suspend fun getSecret(key: String, storage: StorageType): Result<String?> = runCatching {
        when (storage) {
            StorageType.SHARED_PREFERENCES -> sharedPrefs.getString(key, null)
            StorageType.DATA_STORE -> {
                val prefKey = stringPreferencesKey(key)
                context.dataStore.data.first()[prefKey]
            }
            StorageType.ENCRYPTED_SHARED_PREFERENCES -> encryptedPrefs.getString(key, null)
        }
    }
}

