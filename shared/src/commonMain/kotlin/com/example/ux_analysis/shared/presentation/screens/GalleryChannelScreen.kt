package com.example.ux_analysis.shared.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ux_analysis.shared.presentation.theme.DiscordColors

@Composable
fun GalleryChannelScreen(
    nombreCanal: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mock gallery items
    val items = listOf(
        GalleryItem("user1", "Alex", "¡Miren este setup!", "🎨"),
        GalleryItem("user2", "María", "Mi nuevo avatar", "✨"),
        GalleryItem("user3", "Carlos", "Debugging mood", "🐛"),
        GalleryItem("me", "Erick:)", "Discord Clone WIP", "🚀")
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DiscordColors.ChatBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                color = DiscordColors.ChatBackground,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "◀",
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .padding(4.dp),
                        color = DiscordColors.TextSecondary
                    )
                    
                    Text(
                        text = "📷",
                        fontSize = 20.sp,
                        color = DiscordColors.TextSecondary
                    )
                    
                    Text(
                        text = nombreCanal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiscordColors.TextPrimary
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    ImageGalleryItem(item)
                }
            }
        }
    }
}

@Composable
fun ImageGalleryItem(item: GalleryItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DiscordColors.ElevatedBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(DiscordColors.RailBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.imagePlaceholder, fontSize = 64.sp)
        }
        
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = item.authorName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DiscordColors.TextPrimary
            )
            Text(
                text = item.caption,
                fontSize = 12.sp,
                color = DiscordColors.TextSecondary,
                maxLines = 1
            )
        }
    }
}

data class GalleryItem(
    val authorId: String,
    val authorName: String,
    val caption: String,
    val imagePlaceholder: String
)
