package com.example.moviles2026aswgr1.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun AppScreen(useCommonResources: Boolean = false) {
    val textoColor = appTextColor()
    val fondoColor = appBackgroundColor()
    val mensaje = if (useCommonResources) commonSaludo() else appSaludo()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mensaje,
                color = textoColor,
                fontSize = 28.sp
            )
        }
    }
}
