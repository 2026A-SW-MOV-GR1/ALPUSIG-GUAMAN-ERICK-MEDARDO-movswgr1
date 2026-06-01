package com.example.dualpersistence_e1b.util

import java.util.UUID

actual fun randomId(): String = UUID.randomUUID().toString()

actual fun nowEpochMillis(): Long = System.currentTimeMillis()

