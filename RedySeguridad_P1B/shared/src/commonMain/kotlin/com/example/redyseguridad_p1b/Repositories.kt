package com.example.redyseguridad_p1b

interface PostRepository {
    suspend fun getPost(id: Int): Result<Post>
    suspend fun updatePost(id: Int, post: Post): Result<Unit>
}

interface SecretRepository {
    suspend fun saveSecret(entry: SecretEntry): Result<Unit>
    suspend fun getSecret(key: String, storage: StorageType): Result<String?>
}

