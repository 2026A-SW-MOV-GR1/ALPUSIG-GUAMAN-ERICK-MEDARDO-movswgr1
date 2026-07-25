package com.example.gestionpaqueteria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gestionpaqueteria.navigation.AppNavigation
import com.example.gestionpaqueteria.ui.theme.AmazonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AmazonTheme {
                AppNavigation()
            }
        }
    }
}
