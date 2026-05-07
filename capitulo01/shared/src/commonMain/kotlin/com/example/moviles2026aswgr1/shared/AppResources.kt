package com.example.moviles2026aswgr1.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import moviles2026aswgr1.shared.generated.resources.Res
import moviles2026aswgr1.shared.generated.resources.saludo

@Composable
fun commonSaludo(): String = stringResource(Res.string.saludo)

@Composable
expect fun appSaludo(): String

@Composable
expect fun appTextColor(): Color

@Composable
expect fun appBackgroundColor(): Color
