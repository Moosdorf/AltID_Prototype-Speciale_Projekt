package com.example.specialeprojekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.specialeprojekt.theme.SpecialeProjektTheme
import com.example.specialeprojekt.ui.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpecialeProjektTheme {
                NavGraph()
            }
        }

    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Prev() {
        NavGraph()
    }
}



