package com.example.redyseguridad_p1b

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App(
    postRepository: PostRepository,
    secretRepository: SecretRepository
) {
    val postViewModel = remember(postRepository) { PostViewModel(postRepository) }
    val secretViewModel = remember(secretRepository) { SecretViewModel(secretRepository) }

    DisposableEffect(Unit) {
        onDispose {
            postViewModel.close()
            secretViewModel.close()
        }
    }

    MaterialTheme {
        var selectedTab by remember { mutableIntStateOf(0) }
        val tabs = listOf("POST", "SECRETOS")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Column(modifier = Modifier.padding(top = 16.dp)) {
                if (selectedTab == 0) {
                    PostScreen(postViewModel)
                } else {
                    SecretScreen(secretViewModel)
                }
            }
        }
    }
}