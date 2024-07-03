package com.example.media3practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.media3practice.ui.theme.Media3PracticeTheme
import com.example.media3practice.view.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Media3PracticeTheme {
                HomeScreen(1)
            }
        }
    }
}