package com.example.ciclo_de_vida

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CounterViewModel : ViewModel() {
    private val _count = mutableStateOf(0)
    val count: State<Int> = _count

    fun increment() {
        _count.value++
        logEvent("COUNT_INCREMENTED: ${_count.value}")
    }

    fun decrement() {
        if (_count.value > 0) {
            _count.value--
            logEvent("COUNT_DECREMENTED: ${_count.value}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        logEvent("onCleared (ViewModel cleared)")
    }

    companion object {
        fun logEvent(message: String) {
            val timestamp = System.currentTimeMillis()
            println("[LIFECYCLE] [$timestamp] $message")
        }
    }
}
