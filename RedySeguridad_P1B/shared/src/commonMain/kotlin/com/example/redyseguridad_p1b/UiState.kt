package com.example.redyseguridad_p1b

sealed class PostUiState {
    data object Idle : PostUiState()
    data object Loading : PostUiState()
    data class Success(val post: Post) : PostUiState()
    data object UpdateSuccess : PostUiState()
    data class Error(val message: String) : PostUiState()
}

sealed class SecretUiState {
    data object Idle : SecretUiState()
    data object Saving : SecretUiState()
    data object Saved : SecretUiState()
    data class Retrieved(val value: String) : SecretUiState()
    data object NotFound : SecretUiState()
    data class Error(val message: String) : SecretUiState()
}

