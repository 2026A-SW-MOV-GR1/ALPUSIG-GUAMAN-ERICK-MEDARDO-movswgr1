package com.example.redyseguridad_p1b

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow

class PostViewModel(private val repo: PostRepository) {
    var postId by mutableStateOf("")
    var editTitle by mutableStateOf("")
    var editBody by mutableStateOf("")

    val uiState = MutableStateFlow<PostUiState>(PostUiState.Idle)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var loadedPost: Post? = null

    fun fetchPost() {
        val id = postId.trim().toIntOrNull()
        if (id == null) {
            uiState.value = PostUiState.Error("ID invalido")
            return
        }

        uiState.value = PostUiState.Loading
        scope.launch {
            repo.getPost(id)
                .onSuccess { post ->
                    loadedPost = post
                    editTitle = post.title
                    editBody = post.body
                    uiState.value = PostUiState.Success(post)
                }
                .onFailure { error ->
                    uiState.value = PostUiState.Error(error.message ?: "Error de red")
                }
        }
    }

    fun updatePost() {
        val id = postId.trim().toIntOrNull()
        val current = loadedPost
        if (id == null || current == null) {
            uiState.value = PostUiState.Error("Cargue un post antes de actualizar")
            return
        }

        uiState.value = PostUiState.Loading
        val updated = current.copy(title = editTitle, body = editBody)
        scope.launch {
            repo.updatePost(id, updated)
                .onSuccess {
                    uiState.value = PostUiState.UpdateSuccess
                }
                .onFailure { error ->
                    uiState.value = PostUiState.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun close() {
        scope.cancel()
    }
}

