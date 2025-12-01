package com.example.gogym

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.gogym.pages.MyAppNavigation
import com.example.gogym.ui.theme.GoGymTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GoGymTheme {
                MyAppNavigation(
                    modifier = Modifier.fillMaxSize(),
                    authViewModel = authViewModel
                )
            }
        }

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            Log.e("MainActivity", "Uncaught Exception: ", throwable)
        }
    }
}
