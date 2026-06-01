package com.example.dualpersistence_e1b

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.dualpersistence_e1b.ui.DualPersistenceApp
import com.example.dualpersistence_e1b.viewmodel.ItemViewModel

@Composable
fun App(viewModel: ItemViewModel) {
    MaterialTheme {
        DualPersistenceApp(viewModel)
    }
}