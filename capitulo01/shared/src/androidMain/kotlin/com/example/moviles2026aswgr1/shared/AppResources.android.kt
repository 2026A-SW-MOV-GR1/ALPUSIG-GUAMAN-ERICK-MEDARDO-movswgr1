package com.example.moviles2026aswgr1.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource

@Composable
actual fun appSaludo(): String = if (LocalInspectionMode.current) {
    commonSaludo()
} else {
    stringResource(id = R.string.saludo)
}

@Composable
actual fun appTextColor(): Color = colorResource(id = R.color.text_color)

@Composable
actual fun appBackgroundColor(): Color = colorResource(id = R.color.bg_color)
