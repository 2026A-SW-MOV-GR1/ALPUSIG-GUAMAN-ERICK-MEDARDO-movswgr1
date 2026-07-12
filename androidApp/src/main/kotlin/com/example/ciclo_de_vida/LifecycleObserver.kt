package com.example.ciclo_de_vida

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import android.util.Log

class AppLifecycleObserver : DefaultLifecycleObserver {
    companion object {
        private const val TAG = "LIFECYCLE_EVENTS"
        
        fun logEvent(event: String) {
            val timestamp = System.currentTimeMillis()
            val message = "[$timestamp] $event"
            Log.d(TAG, message)
            println("[LIFECYCLE] $message")
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        logEvent("onCreate - Activity created")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        logEvent("onStart - Activity is starting")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        logEvent("onResume - Activity became visible and interactive")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        logEvent("onPause - Activity is about to lose focus")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        logEvent("onStop - Activity is no longer visible")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        logEvent("onDestroy - Activity is being destroyed (rotation or exit)")
    }
}

// Nota: onRestart es un callback que ocurre ANTES de onStart cuando la actividad vuelve
// del background. Se maneja en MainActivity directamente.
