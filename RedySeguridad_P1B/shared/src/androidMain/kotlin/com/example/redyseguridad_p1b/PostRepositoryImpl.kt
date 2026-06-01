package com.example.redyseguridad_p1b

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PostRepositoryImpl : PostRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val baseUrl = "https://jsonplaceholder.typicode.com"
    private val cache = mutableMapOf<Int, Post>()

    override suspend fun getPost(id: Int): Result<Post> = runCatching {
        cache[id] ?: client.get("$baseUrl/posts/$id").body()
    }

    override suspend fun updatePost(id: Int, post: Post): Result<Unit> = runCatching {
        val response = client.put("$baseUrl/posts/$id") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }
        if (response.status != HttpStatusCode.OK) {
            error("HTTP ${response.status.value}")
        }
        cache[id] = post
    }
}
