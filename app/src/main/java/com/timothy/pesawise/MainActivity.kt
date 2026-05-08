package com.timothy.pesawise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.timothy.pesawise.navigation.PesaNavGraph
import com.timothy.pesawise.ui.theme.BackgroundGray
import com.timothy.pesawise.ui.theme.PesaWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PesaWiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = BackgroundGray
                ) {
                    PesaNavGraph()
                }
            }
        }
    }
}
