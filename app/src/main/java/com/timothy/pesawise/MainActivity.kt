package com.timothy.pesawise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.timothy.pesawise.navigation.AppNavHost
import com.timothy.pesawise.ui.theme.PesaWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PesaWiseTheme {
                AppNavHost()
            }
        }
    }
}
