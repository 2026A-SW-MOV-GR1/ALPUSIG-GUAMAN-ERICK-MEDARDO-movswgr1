package com.example.ciclo_de_vida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log

class MainActivity : ComponentActivity() {
    private val lifecycleObserver = AppLifecycleObserver()
    companion object {
        private const val TAG = "LIFECYCLE_EVENTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppLifecycleObserver.logEvent("onCreate (MainActivity.onCreate called)")
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Registrar observer del ciclo de vida
        lifecycle.addObserver(lifecycleObserver)
        
        // Logs de información sobre rotación
        if (savedInstanceState != null) {
            AppLifecycleObserver.logEvent("onCreate - savedInstanceState restored (rotation detected)")
        } else {
            AppLifecycleObserver.logEvent("onCreate - First time launch (no savedInstanceState)")
        }

        setContent {
            App()
        }
    }

    override fun onStart() {
        AppLifecycleObserver.logEvent("onStart (MainActivity.onStart called)")
        super.onStart()
    }

    override fun onResume() {
        AppLifecycleObserver.logEvent("onResume (MainActivity.onResume called)")
        super.onResume()
    }

    override fun onPause() {
        AppLifecycleObserver.logEvent("onPause (MainActivity.onPause called)")
        super.onPause()
    }

    override fun onStop() {
        AppLifecycleObserver.logEvent("onStop (MainActivity.onStop called)")
        super.onStop()
    }

    override fun onRestart() {
        AppLifecycleObserver.logEvent("onRestart - Activity returning from background")
        super.onRestart()
    }

    override fun onDestroy() {
        AppLifecycleObserver.logEvent("onDestroy - Activity is being destroyed")
        super.onDestroy()
        lifecycle.removeObserver(lifecycleObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        AppLifecycleObserver.logEvent("onSaveInstanceState - Saving state before destruction")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        AppLifecycleObserver.logEvent("onRestoreInstanceState - Restoring state after recreation")
        super.onRestoreInstanceState(savedInstanceState)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}