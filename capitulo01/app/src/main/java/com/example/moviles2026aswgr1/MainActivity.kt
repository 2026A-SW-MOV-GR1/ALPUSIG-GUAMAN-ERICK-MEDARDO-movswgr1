package com.example.moviles2026aswgr1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme{
                PantallaConfiguración()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContent {
            MaterialTheme{
                PantallaConfiguración()
            }
        }
    }
}

@Composable
fun PantallaConfiguración() {
    val textoColor = colorResource(id = R.color.text_color)
    val fondoColor = colorResource(id = R.color.bg_color)
    val mensaje = stringResource(id = R.string.saludo)

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