package com.example.redyseguridad_p1b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val postRepository = remember { PostRepositoryImpl() }
            val secretRepository = remember { SecretRepositoryImpl(this@MainActivity) }
            App(postRepository, secretRepository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(PreviewPostRepository(), PreviewSecretRepository())
}

private class PreviewPostRepository : PostRepository {
    override suspend fun getPost(id: Int): Result<Post> {
        return Result.success(Post(id = id, userId = 1, title = "Titulo demo", body = "Contenido demo"))
    }

    override suspend fun updatePost(id: Int, post: Post): Result<Unit> {
        return Result.success(Unit)
    }
}

private class PreviewSecretRepository : SecretRepository {
    override suspend fun saveSecret(entry: SecretEntry): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getSecret(key: String, storage: StorageType): Result<String?> {
        return Result.success("valor_demo")
    }
}